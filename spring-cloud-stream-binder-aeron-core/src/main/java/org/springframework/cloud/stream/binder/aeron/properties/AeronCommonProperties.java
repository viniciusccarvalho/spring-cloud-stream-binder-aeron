package org.springframework.cloud.stream.binder.aeron.properties;

/**
 * @author Vinicius Carvalho
 */
public abstract class AeronCommonProperties {

	protected String host = "localhost";

	protected Integer port = -1;

	protected Integer streamId = 1;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public Integer getStreamId() {
		return streamId;
	}

	public void setStreamId(Integer streamId) {
		this.streamId = streamId;
	}
}
