package org.springframework.cloud.stream.binder.aeron.admin;

/**
 * @author Vinicius Carvalho
 */
public interface DestinationRegistryClient {

	void register(AeronChannelInformation channelInformation);

	AeronChannelInformation locate(String destinationName);

	void registerListerner(DestinationRegistrationListener listener);
}
