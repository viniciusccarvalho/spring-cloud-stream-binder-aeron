package org.springframework.cloud.stream.binder.aeron.admin;

/**
 * @author Vinicius Carvalho
 */
public interface DestinationRegistrationListener {
	void onEvent(DestinationRegistrationEvent event);
	String getDestinationPattern();
}
