package org.springframework.cloud.stream.binder.aeron.properties;


import java.util.LinkedList;
import java.util.List;

/**
 * @author Vinicius Carvalho
 */
public class AeronProducerProperties{

	private String controlHost = "localhost";

	private Integer controlPort = -1;

	private Integer streamId = 1;

	private List<String> destinations = new LinkedList<>();

	public String getControlHost() {
		return this.controlHost;
	}

	public void setControlHost(String controlHost) {
		this.controlHost = controlHost;
	}

	public Integer getControlPort() {
		return this.controlPort;
	}

	public void setControlPort(Integer controlPort) {
		this.controlPort = controlPort;
	}

	public Integer getStreamId() {
		return this.streamId;
	}

	public void setStreamId(Integer streamId) {
		this.streamId = streamId;
	}

	public List<String> getDestinations() {
		return this.destinations;
	}

	public void setDestinations(List<String> destinations) {
		this.destinations = destinations;
	}
}
