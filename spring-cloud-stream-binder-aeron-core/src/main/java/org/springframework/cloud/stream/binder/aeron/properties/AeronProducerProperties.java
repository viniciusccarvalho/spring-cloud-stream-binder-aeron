package org.springframework.cloud.stream.binder.aeron.properties;


import java.util.LinkedList;
import java.util.List;

/**
 * @author Vinicius Carvalho
 */
public class AeronProducerProperties extends AeronCommonProperties{


	private List<String> destinations = new LinkedList<>();


	public List<String> getDestinations() {
		return this.destinations;
	}

	public void setDestinations(List<String> destinations) {
		this.destinations = destinations;
	}
}
