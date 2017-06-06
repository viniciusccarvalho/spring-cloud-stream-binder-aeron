package org.springframework.cloud.stream.binder.aeron.config;

import java.util.LinkedList;
import java.util.List;

import io.aeron.Aeron;
import io.aeron.driver.MediaDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.binder.Binder;
import org.springframework.cloud.stream.binder.aeron.AeronMessageChannelBinder;
import org.springframework.cloud.stream.binder.aeron.admin.RedisDestinationRegistryClient;
import org.springframework.cloud.stream.binder.aeron.properties.AeronExtendedBindingProperties;
import org.springframework.cloud.stream.binder.aeron.provisioning.AeronDestinationProvisioner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Vinicius Carvalho
 */
@Configuration
@ConditionalOnClass(Aeron.class)
@ConditionalOnMissingBean(Binder.class)
@EnableConfigurationProperties({AeronExtendedBindingProperties.class})
@EnableScheduling
public class AeronBinderAutoConfiguration {

	private Logger logger = LoggerFactory.getLogger(AeronBinderAutoConfiguration.class);

	@Autowired
	private AeronExtendedBindingProperties extendedBindingProperties;

	@Autowired
	private RedisConnectionFactory redisConnectionFactory;

	@Bean(destroyMethod = "close")
	@ConditionalOnMissingBean
	public MediaDriver mediaDriver(){
		return MediaDriver.launchEmbedded();
	}

	@Bean(destroyMethod = "close")
	@ConditionalOnMissingBean
	public Aeron aeron(){
		Aeron.Context ctx = new Aeron.Context();
		MediaDriver driver = mediaDriver();
		logger.info("Creating Aeron context using directory: {}",driver.aeronDirectoryName());
		ctx.aeronDirectoryName(driver.aeronDirectoryName());
		return Aeron.connect(ctx);
	}

	public AeronDestinationProvisioner provisioningProvider(){
		return new AeronDestinationProvisioner();
	}

	@Bean
	public AeronMessageChannelBinder binder() throws Exception {
		AeronMessageChannelBinder binder = new AeronMessageChannelBinder(provisioningProvider(),aeron());
		binder.setExtendedBindingProperties(this.extendedBindingProperties);
		binder.setDestinationRegistryClient(destinationRegistryClient());
		return binder;
	}

	@Bean
	public RedisMessageListenerContainer redisMessageListenerContainer() {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(redisConnectionFactory);
		List<Topic> topics = new LinkedList<>();
		topics.add( new PatternTopic(RedisDestinationRegistryClient.expires_pattern));
		topics.add(new PatternTopic(RedisDestinationRegistryClient.set_pattern));
		container.addMessageListener(destinationRegistryClient(), topics);
		return container;
	}


	@Bean
	public RedisDestinationRegistryClient destinationRegistryClient(){
		RedisDestinationRegistryClient redisDestinationRegistryClient = new RedisDestinationRegistryClient(redisConnectionFactory);
		return redisDestinationRegistryClient;
	}

}
