package org.springframework.cloud.stream.binder.aeron.properties;

/**
 * @author Vinicius Carvalho
 */
public class AeronConsumerProperties {

	private Integer port;

	private Integer streamId = 1;

	public Integer getStreamId() {
		return this.streamId;
	}

	public void setStreamId(Integer streamId) {
		this.streamId = streamId;
	}

	public Integer getPort() {
		return this.port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}
}
