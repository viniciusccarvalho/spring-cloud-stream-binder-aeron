package org.springframework.cloud.stream.binder.aeron.properties;

/**
 * @author Vinicius Carvalho
 */
public class AeronBindingProperties {

	private AeronConsumerProperties consumerProperties = new AeronConsumerProperties();
	private AeronProducerProperties producerProperties = new AeronProducerProperties();

	public AeronConsumerProperties getConsumerProperties() {
		return consumerProperties;
	}

	public void setConsumerProperties(AeronConsumerProperties consumerProperties) {
		this.consumerProperties = consumerProperties;
	}

	public AeronProducerProperties getProducerProperties() {
		return producerProperties;
	}

	public void setProducerProperties(AeronProducerProperties producerProperties) {
		this.producerProperties = producerProperties;
	}
}
