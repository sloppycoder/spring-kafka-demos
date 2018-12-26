package net.vino9.kafka.producer;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProducerApplicationTests {

	private  static String TEST_TOPIC = "dummy";

	@ClassRule
	public static EmbeddedKafkaRule broker = new EmbeddedKafkaRule(1, false, TEST_TOPIC);
	@Autowired
	CardTransactionProducer producer;

	@BeforeClass
	public static void setup() {
		System.setProperty("spring.kafka.bootstrap-servers",
				broker.getEmbeddedKafka().getBrokersAsString());
	}

	@SuppressWarnings("EmptyMethod")
	@Test
	public void contextLoads() {
	}

	@Test
	public void can_publish_from_csv() throws IOException {
		producer.publishAllTransactions(TEST_TOPIC, "/Users/lee/tmp/alltran/AllTransactions_0827181438_0827181508.csv");
	}

}

