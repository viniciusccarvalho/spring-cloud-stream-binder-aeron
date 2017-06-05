package org.springframework.cloud.stream.binder.aeron.provisioning;

import org.springframework.cloud.stream.binder.aeron.admin.DestinationRegistryClient;
import org.springframework.cloud.stream.binder.aeron.properties.AeronConsumerProperties;
import org.springframework.cloud.stream.provisioning.ConsumerDestination;

/**
 * @author Vinicius Carvalho
 */
public class AeronConsumerDestination implements ConsumerDestination {

	private AeronConsumerProperties consumerProperties;

	private final String consumerConnection = "aeron:udp?endpoint=%s:%d";

	private DestinationRegistryClient registryClient;

	public AeronConsumerDestination(AeronConsumerProperties consumerProperties) {
		this(consumerProperties,null);
	}

	public AeronConsumerDestination(AeronConsumerProperties consumerProperties, DestinationRegistryClient registryClient) {
		this.consumerProperties = consumerProperties;
		this.registryClient = registryClient;
	}

	@Override
	public String getName() {
		return String.format(this.consumerConnection, this.consumerProperties.getHost(), this.consumerProperties.getPort());
	}
}
