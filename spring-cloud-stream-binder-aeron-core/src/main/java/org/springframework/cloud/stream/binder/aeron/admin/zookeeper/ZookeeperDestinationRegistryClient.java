package org.springframework.cloud.stream.binder.aeron.admin.zookeeper;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.stream.binder.aeron.admin.AeronChannelInformation;
import org.springframework.cloud.stream.binder.aeron.admin.DestinationRegistrationListener;
import org.springframework.cloud.stream.binder.aeron.admin.DestinationRegistryClient;
import org.springframework.cloud.zookeeper.discovery.ZookeeperInstance;
import org.springframework.cloud.zookeeper.discovery.watcher.DependencyState;
import org.springframework.cloud.zookeeper.discovery.watcher.DependencyWatcherListener;
import org.springframework.cloud.zookeeper.serviceregistry.ServiceInstanceRegistration;
import org.springframework.cloud.zookeeper.serviceregistry.ZookeeperRegistration;
import org.springframework.cloud.zookeeper.serviceregistry.ZookeeperServiceRegistry;

/**
 * @author Vinicius Carvalho
 */
public class ZookeeperDestinationRegistryClient  implements DestinationRegistryClient, DependencyWatcherListener{

	private final String path = "/spring/cloud/stream/aeron/destinations/";

	private List<DestinationRegistrationListener> listeners = new LinkedList();

	private ZookeeperServiceRegistry serviceRegistry;

	public ZookeeperDestinationRegistryClient(ZookeeperServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

	@Override
	public void register(AeronChannelInformation channelInformation) {
		Map<String,String> metadata = new HashMap<>();
		metadata.put("STREAM_ID",channelInformation.getStreamId().toString());
		ZookeeperInstance instance = new ZookeeperInstance("aeron_metdata","aeron",metadata);
		ZookeeperRegistration registration = ServiceInstanceRegistration.builder()
				.defaultUriSpec()
				.address(channelInformation.getHost())
				.port(channelInformation.getPort())
				.name(channelInformation.getDestinationName())
				.payload(instance)
				.build();
		this.serviceRegistry.register(registration);

	}

	@Override
	public AeronChannelInformation locate(String destinationName) {

		return null;
	}

	@Override
	public void registerListerner(DestinationRegistrationListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public void stateChanged(String dependency, DependencyState dependencyState) {
		switch (dependencyState){
			case CONNECTED:

				break;
			case DISCONNECTED:
				break;
		}
	}
}
