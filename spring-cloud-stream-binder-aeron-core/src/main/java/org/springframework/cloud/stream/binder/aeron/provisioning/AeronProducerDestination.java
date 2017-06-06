package org.springframework.cloud.stream.binder.aeron.provisioning;

import java.util.Collections;
import java.util.List;

import org.springframework.cloud.stream.binder.aeron.properties.AeronProducerProperties;
import org.springframework.cloud.stream.provisioning.ProducerDestination;

/**
 * @author Vinicius Carvalho
 */
public class AeronProducerDestination implements ProducerDestination {

	private final String controlConnection = "aeron:udp?control=%s:%d|control-mode=manual";

	private AeronProducerProperties producerProperties;

	private String channelName;

	public AeronProducerDestination(String channelName, AeronProducerProperties producerProperties) {
		this.producerProperties = producerProperties;
		this.channelName = channelName;
	}

	@Override
	/**
	 * Returns the control channel configuration for a Producer. Use getDestinations() for the list of possible destinations
	 */
	public String getName() {
		return String.format(this.controlConnection, this.producerProperties.getHost(), this.producerProperties.getPort());
	}

	@Override
	public String getNameForPartition(int i) {
		return null;
	}

	public Integer getStreamId(){
		return this.producerProperties.getStreamId();
	}

	public List<String> getDestinations(){
		return Collections.unmodifiableList(this.producerProperties.getDestinations());
	}

	public String getChannelName() {
		return channelName;
	}
}
