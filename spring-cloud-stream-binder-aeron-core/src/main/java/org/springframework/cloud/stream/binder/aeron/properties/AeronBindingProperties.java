package org.springframework.cloud.stream.binder.aeron.properties;

/**
 * @author Vinicius Carvalho
 */
public class AeronBindingProperties {

	private AeronConsumerProperties consumer = new AeronConsumerProperties();
	private AeronProducerProperties producer = new AeronProducerProperties();

	public AeronConsumerProperties getConsumer() {
		return this.consumer;
	}

	public void setConsumer(AeronConsumerProperties consumer) {
		this.consumer = consumer;
	}

	public AeronProducerProperties getProducer() {
		return this.producer;
	}

	public void setProducer(AeronProducerProperties producer) {
		this.producer = producer;
	}
}
