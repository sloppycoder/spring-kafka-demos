package net.vino9.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class ProducerApplication implements CommandLineRunner {

	@Autowired
	private CardTransactionProducer producer;

	@Value("${topics.card-transaction}")
	private String topic;

	public static void main(String[] args) {
		SpringApplication.run(ProducerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		int iterations = 1;

		if (args.length > 1) {
			iterations = Integer.parseInt(args[1]);
		}

		for (int i = 0; i < iterations; i++) {
			producer.publishAllTransactions(topic, "/Users/lee/tmp/alltran/AllTransactions_0827181438_0827181508.csv");
		}
	}
}

