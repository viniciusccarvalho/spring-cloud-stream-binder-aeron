package org.springframework.cloud.stream.binder.aeron.provisioning;

import org.springframework.cloud.stream.binder.ExtendedConsumerProperties;
import org.springframework.cloud.stream.binder.ExtendedProducerProperties;
import org.springframework.cloud.stream.binder.aeron.properties.AeronConsumerProperties;
import org.springframework.cloud.stream.binder.aeron.properties.AeronProducerProperties;
import org.springframework.cloud.stream.provisioning.ConsumerDestination;
import org.springframework.cloud.stream.provisioning.ProducerDestination;
import org.springframework.cloud.stream.provisioning.ProvisioningException;
import org.springframework.cloud.stream.provisioning.ProvisioningProvider;

/**
 * @author Vinicius Carvalho
 */
public class AeronDestinationProvisioner implements
		ProvisioningProvider<ExtendedConsumerProperties<AeronConsumerProperties>, ExtendedProducerProperties<AeronProducerProperties>> {

	@Override
	public ProducerDestination provisionProducerDestination(String name,
			ExtendedProducerProperties<AeronProducerProperties> producerProperties)
			throws ProvisioningException {
		return null;
	}

	@Override
	public ConsumerDestination provisionConsumerDestination(String name, String group,
			ExtendedConsumerProperties<AeronConsumerProperties> consumerProperties)
			throws ProvisioningException {
		return null;
	}
}
