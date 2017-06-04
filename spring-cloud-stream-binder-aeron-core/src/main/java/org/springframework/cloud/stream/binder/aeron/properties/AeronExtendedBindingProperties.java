package org.springframework.cloud.stream.binder.aeron.properties;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.stream.binder.ExtendedBindingProperties;

/**
 * @author Vinicius Carvalho
 */
@ConfigurationProperties("spring.cloud.stream.aeron")
public class AeronExtendedBindingProperties implements ExtendedBindingProperties<AeronConsumerProperties,AeronProducerProperties>{

	private Map<String,AeronBindingProperties> bindings = new HashMap<>();

	public Map<String, AeronBindingProperties> getBindings() {
		return bindings;
	}

	public void setBindings(Map<String, AeronBindingProperties> bindings) {
		this.bindings = bindings;
	}

	@Override
	public AeronConsumerProperties getExtendedConsumerProperties(String channelName) {
		if (bindings.containsKey(channelName) && bindings.get(channelName).getConsumerProperties() != null) {
			return bindings.get(channelName).getConsumerProperties();
		}
		else {
			return new AeronConsumerProperties();
		}
	}

	@Override
	public AeronProducerProperties getExtendedProducerProperties(String channelName) {
		if (bindings.containsKey(channelName) && bindings.get(channelName).getProducerProperties() != null) {
			return bindings.get(channelName).getProducerProperties();
		}
		else {
			return new AeronProducerProperties();
		}
	}
}
