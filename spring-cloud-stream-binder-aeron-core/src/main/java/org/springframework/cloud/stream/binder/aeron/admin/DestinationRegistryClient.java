package org.springframework.cloud.stream.binder.aeron.admin;

import java.util.List;

/**
 * @author Vinicius Carvalho
 */
public interface DestinationRegistryClient {

	void register(AeronChannelInformation channelInformation);

	List<AeronChannelInformation> locate(String destinationName);

	void registerListerner(DestinationRegistrationListener listener);
}
