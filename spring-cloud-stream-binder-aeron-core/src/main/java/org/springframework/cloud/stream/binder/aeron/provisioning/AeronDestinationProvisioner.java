package org.springframework.cloud.stream.binder.aeron.provisioning;

import org.springframework.cloud.stream.binder.ExtendedConsumerProperties;
import org.springframework.cloud.stream.binder.ExtendedProducerProperties;
import org.springframework.cloud.stream.binder.aeron.properties.AeronConsumerProperties;
import org.springframework.cloud.stream.binder.aeron.properties.AeronProducerProperties;
import org.springframework.cloud.stream.provisioning.ConsumerDestination;
import org.springframework.cloud.stream.provisioning.ProducerDestination;
import org.springframework.cloud.stream.provisioning.ProvisioningException;
import org.springframework.cloud.stream.provisioning.ProvisioningProvider;
import org.springframework.util.SocketUtils;

/**
 * @author Vinicius Carvalho
 */
public class AeronDestinationProvisioner implements
		ProvisioningProvider<ExtendedConsumerProperties<AeronConsumerProperties>, ExtendedProducerProperties<AeronProducerProperties>> {

	@Override
	public ProducerDestination provisionProducerDestination(String name,
			ExtendedProducerProperties<AeronProducerProperties> producerProperties)
			throws ProvisioningException {
		if(producerProperties.getExtension().getPort() == -1){
			producerProperties.getExtension().setPort(SocketUtils.findAvailableTcpPort(43_000,43_100));
		}
		return new AeronProducerDestination(producerProperties.getExtension());
	}

	@Override
	public ConsumerDestination provisionConsumerDestination(String name, String group,
			ExtendedConsumerProperties<AeronConsumerProperties> consumerProperties)
			throws ProvisioningException {
		if(consumerProperties.getExtension().getPort() == -1){
			consumerProperties.getExtension().setPort(SocketUtils.findAvailableTcpPort(42_000,42_100));
		}
		return new AeronConsumerDestination(consumerProperties.getExtension());
	}
}
