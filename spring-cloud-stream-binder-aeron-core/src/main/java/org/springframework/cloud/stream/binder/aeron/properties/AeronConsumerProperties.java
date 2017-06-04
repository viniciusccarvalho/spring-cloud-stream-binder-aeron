package org.springframework.cloud.stream.binder.aeron.properties;

/**
 * @author Vinicius Carvalho
 */
public class AeronConsumerProperties {

	private Integer port;

	private Integer streamId = 1;

	public Integer getStreamId() {
		return streamId;
	}

	public void setStreamId(Integer streamId) {
		this.streamId = streamId;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}
}
