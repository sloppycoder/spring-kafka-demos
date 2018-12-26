package net.vino9.kafka.producer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.StreamSupport;

import net.vino9.kafka.models.CardTransaction;
import net.vino9.kafka.models.Merchant;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class CardTransactionProducer {

	private static final Logger logger = LoggerFactory.getLogger(CardTransactionProducer.class);

	private final KafkaTemplate<Object, Object> template;

	@Autowired
	public CardTransactionProducer(KafkaTemplate<Object, Object> template) {
		this.template = template;
	}

	public void publishAllTransactions(String topic, String path) throws IOException {
		// publish all transactions in the CSV file
		BufferedReader reader = new BufferedReader(new FileReader(path));
		for (int i = 0; i < 6; i++) {
			reader.readLine();
		}

		Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
		StreamSupport.stream(records.spliterator(), false).forEach(rec -> {
			try {
				CardTransaction transaction = parseTransactionDetails(rec);
				template.send(topic, transaction).get(5, TimeUnit.SECONDS);
			}
			catch (IllegalArgumentException e) {
				logger.info("Bad csv record {}", rec.getRecordNumber());
			}
			catch (ExecutionException e) {
				logger.info("ExecutionException {}", e);
			}
			catch (InterruptedException | TimeoutException e) {
				logger.info("Exception {}", e);
			}

		});

		logMetrics();

	}

	private CardTransaction parseTransactionDetails(CSVRecord rec) {
		CardTransaction transaction = new CardTransaction();

		Merchant merchant = new Merchant();
		merchant.setId(rec.get(" Merchant ID"));
		merchant.setName(rec.get(" Merchant Name"));
		merchant.setCountry(rec.get(" Merchant Country"));
		merchant.setUrl(rec.get(" Merchant URL"));

		transaction.setId(rec.get(" Transaction ProxyPAN"));
		transaction.setCardNumber(rec.get(" Card Number"));
		transaction.setAmount(Double.valueOf(rec.get(" Amount")));
		transaction.setCurrency(rec.get(" Currency"));
		transaction.setMerchant(merchant);
		transaction.setCalloutStatus(rec.get(" CallOut Status"));

		return transaction;
	}

	private void logMetrics() {
		Map<MetricName, ? extends Metric> metrics = template.metrics();
		logger.info("================================ metrics =============================");
		metrics.forEach((metricName, metricValue) ->
			logger.info(String.format("%40s : %26s", metricName.name(), metricValue.metricValue())));
	}
}
