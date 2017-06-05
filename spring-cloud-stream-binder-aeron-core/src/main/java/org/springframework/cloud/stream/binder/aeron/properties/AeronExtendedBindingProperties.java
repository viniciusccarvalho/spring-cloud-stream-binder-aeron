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
		return this.bindings;
	}

	public void setBindings(Map<String, AeronBindingProperties> bindings) {
		this.bindings = bindings;
	}

	@Override
	public AeronConsumerProperties getExtendedConsumerProperties(String channelName) {
		if (this.bindings.containsKey(channelName) && this.bindings.get(channelName).getConsumer() != null) {
			return this.bindings.get(channelName).getConsumer();
		}
		else {
			return new AeronConsumerProperties();
		}
	}

	@Override
	public AeronProducerProperties getExtendedProducerProperties(String channelName) {
		if (this.bindings.containsKey(channelName) && this.bindings.get(channelName).getProducer() != null) {
			return this.bindings.get(channelName).getProducer();
		}
		else {
			return new AeronProducerProperties();
		}
	}
}
