package org.springframework.cloud.stream.binder.aeron;

import io.aeron.Aeron;

import org.springframework.cloud.stream.binder.AbstractMessageChannelBinder;
import org.springframework.cloud.stream.binder.ExtendedConsumerProperties;
import org.springframework.cloud.stream.binder.ExtendedProducerProperties;
import org.springframework.cloud.stream.binder.ExtendedPropertiesBinder;
import org.springframework.cloud.stream.binder.aeron.admin.AeronChannelInformation;
import org.springframework.cloud.stream.binder.aeron.admin.DestinationRegistryClient;
import org.springframework.cloud.stream.binder.aeron.properties.AeronConsumerProperties;
import org.springframework.cloud.stream.binder.aeron.properties.AeronExtendedBindingProperties;
import org.springframework.cloud.stream.binder.aeron.properties.AeronProducerProperties;
import org.springframework.cloud.stream.binder.aeron.provisioning.AeronConsumerDestination;
import org.springframework.cloud.stream.binder.aeron.provisioning.AeronDestinationProvisioner;
import org.springframework.cloud.stream.binder.aeron.provisioning.AeronProducerDestination;
import org.springframework.cloud.stream.provisioning.ConsumerDestination;
import org.springframework.cloud.stream.provisioning.ProducerDestination;
import org.springframework.integration.core.MessageProducer;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

/**
 * @author Vinicius Carvalho
 */
public class AeronMessageChannelBinder extends AbstractMessageChannelBinder<ExtendedConsumerProperties<AeronConsumerProperties>,
		ExtendedProducerProperties<AeronProducerProperties>,
		AeronDestinationProvisioner>
		implements ExtendedPropertiesBinder<MessageChannel, AeronConsumerProperties, AeronProducerProperties>{

	private AeronExtendedBindingProperties extendedBindingProperties = new AeronExtendedBindingProperties();

	private AeronDestinationProvisioner provisioner;

	private DestinationRegistryClient destinationRegistryClient;

	private Aeron aeron;

	public AeronMessageChannelBinder(AeronDestinationProvisioner provisioningProvider, Aeron aeron) {
		super(false, new String[0], provisioningProvider);
		this.aeron = aeron;
		this.provisioner = provisioningProvider;
	}



	@Override
	protected MessageHandler createProducerMessageHandler(ProducerDestination producerDestination, ExtendedProducerProperties<AeronProducerProperties> extendedProducerProperties) throws Exception {
		AeronMessageHandler messageHandler = new AeronMessageHandler((AeronProducerDestination)producerDestination,aeron);
		if(this.destinationRegistryClient != null){
			this.destinationRegistryClient.registerListerner(messageHandler);
		}
		return messageHandler;
	}

	@Override
	protected MessageProducer createConsumerEndpoint(ConsumerDestination consumerDestination, String group, ExtendedConsumerProperties<AeronConsumerProperties> consumerProperties) throws Exception {
		AeronMessageContainer messageContainer = new AeronMessageContainer(this.aeron,consumerDestination.getName(),consumerProperties.getExtension().getStreamId());
		if(this.destinationRegistryClient != null){
			AeronChannelInformation information = AeronChannelInformation.newBuilder()
					.host(consumerProperties.getExtension().getHost())
					.port(consumerProperties.getExtension().getPort())
					.streamId(consumerProperties.getExtension().getStreamId())
					.destination(((AeronConsumerDestination)consumerDestination).getChannelName()).build();
			this.destinationRegistryClient.register(information);
		}
		return messageContainer;
	}

	@Override
	public AeronConsumerProperties getExtendedConsumerProperties(String channelName) {
		return this.extendedBindingProperties.getExtendedConsumerProperties(channelName);
	}

	@Override
	public AeronProducerProperties getExtendedProducerProperties(String channelName) {
		return this.extendedBindingProperties.getExtendedProducerProperties(channelName);
	}

	public void setExtendedBindingProperties(AeronExtendedBindingProperties extendedBindingProperties) {
		this.extendedBindingProperties = extendedBindingProperties;
	}

	public void setDestinationRegistryClient(DestinationRegistryClient destinationRegistryClient) {
		this.destinationRegistryClient = destinationRegistryClient;
	}
}
