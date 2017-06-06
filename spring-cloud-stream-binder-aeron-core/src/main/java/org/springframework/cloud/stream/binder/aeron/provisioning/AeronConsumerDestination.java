package org.springframework.cloud.stream.binder.aeron.provisioning;

import org.springframework.cloud.stream.binder.aeron.admin.AeronUtils;
import org.springframework.cloud.stream.binder.aeron.properties.AeronConsumerProperties;
import org.springframework.cloud.stream.provisioning.ConsumerDestination;

/**
 * @author Vinicius Carvalho
 */
public class AeronConsumerDestination implements ConsumerDestination {

	private AeronConsumerProperties consumerProperties;

	private final String consumerConnection = "aeron:udp?endpoint=%s:%d";

	private String channelName;

	public AeronConsumerDestination(AeronConsumerProperties consumerProperties, String channelName) {
		this.consumerProperties = consumerProperties;
		this.channelName = channelName;
	}


	@Override
	public String getName() {
		return AeronUtils.consumerConnectionString(this.consumerProperties.getHost(), this.consumerProperties.getPort());
	}

	public String getChannelName() {
		return channelName;
	}
}
