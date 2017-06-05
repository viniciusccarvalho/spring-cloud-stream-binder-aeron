package org.springframework.cloud.stream.binder.aeron;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.netflix.archaius.ArchaiusAutoConfiguration;
import org.springframework.cloud.netflix.rx.RxJavaAutoConfiguration;
import org.springframework.cloud.stream.binder.aeron.admin.AeronChannelInformation;
import org.springframework.cloud.stream.binder.aeron.admin.zookeeper.ZookeeperDestinationRegistryClient;
import org.springframework.cloud.zookeeper.serviceregistry.ZookeeperServiceRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Vinicius Carvalho
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class ZookeeperDestinationRegistryClientTest {

	@Autowired
	private ZookeeperDestinationRegistryClient registryClient;

	@Test
	public void contextLoads() throws Exception {

		this.registryClient.register(AeronChannelInformation.newBuilder().host("192.168.86.17").port(42000).streamId(1).destination("output").build());
	}

	@SpringBootApplication
	@EnableAutoConfiguration(exclude = {RxJavaAutoConfiguration.class, ArchaiusAutoConfiguration.class})
	public static class ApplicationTest {

		@Autowired
		private ZookeeperServiceRegistry serviceRegistry;

		@Bean
		public ZookeeperDestinationRegistryClient destinationRegistryClient(){
			return new ZookeeperDestinationRegistryClient(serviceRegistry);
		}
	}

}
