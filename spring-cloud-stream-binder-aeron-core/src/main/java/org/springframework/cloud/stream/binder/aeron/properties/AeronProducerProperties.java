package org.springframework.cloud.stream.binder.aeron.properties;

import org.springframework.cloud.stream.binder.ExtendedConsumerProperties;
import org.springframework.cloud.stream.binder.ExtendedProducerProperties;
import org.springframework.cloud.stream.provisioning.ConsumerDestination;
import org.springframework.cloud.stream.provisioning.ProducerDestination;
import org.springframework.cloud.stream.provisioning.ProvisioningException;
import org.springframework.cloud.stream.provisioning.ProvisioningProvider;

/**
 * @author Vinicius Carvalho
 */
public class AeronProducerProperties implements ProvisioningProvider<ExtendedConsumerProperties<AeronConsumerProperties>, ExtendedProducerProperties<AeronProducerProperties>> {

	@Override
	public ProducerDestination provisionProducerDestination(String s, ExtendedProducerProperties<AeronProducerProperties> aeronProducerPropertiesExtendedProducerProperties) throws ProvisioningException {
		return null;
	}

	@Override
	public ConsumerDestination provisionConsumerDestination(String s, String s1, ExtendedConsumerProperties<AeronConsumerProperties> aeronConsumerPropertiesExtendedConsumerProperties) throws ProvisioningException {
		return null;
	}

}
