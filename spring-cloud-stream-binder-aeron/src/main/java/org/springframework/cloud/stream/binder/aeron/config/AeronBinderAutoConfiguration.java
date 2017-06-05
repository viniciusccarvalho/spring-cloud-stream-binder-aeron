package org.springframework.cloud.stream.binder.aeron.config;

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
import org.springframework.cloud.stream.binder.aeron.properties.AeronExtendedBindingProperties;
import org.springframework.cloud.stream.binder.aeron.provisioning.AeronDestinationProvisioner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Vinicius Carvalho
 */
@Configuration
@ConditionalOnClass(Aeron.class)
@ConditionalOnMissingBean(Binder.class)
@EnableConfigurationProperties({AeronExtendedBindingProperties.class})
public class AeronBinderAutoConfiguration {

	private Logger logger = LoggerFactory.getLogger(AeronBinderAutoConfiguration.class);

	@Autowired
	private AeronExtendedBindingProperties extendedBindingProperties;

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
		return binder;
	}
}
