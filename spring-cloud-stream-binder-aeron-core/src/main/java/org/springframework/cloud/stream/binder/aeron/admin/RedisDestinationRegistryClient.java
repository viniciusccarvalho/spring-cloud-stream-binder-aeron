package org.springframework.cloud.stream.binder.aeron.admin;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;

/**
 * @author Vinicius Carvalho
 */
public class RedisDestinationRegistryClient implements DestinationRegistryClient, MessageListener, InitializingBean{

	private RedisTemplate<String,AeronChannelInformation> redisTemplate;

	public static final String PREFIX = "aeron.destination";

	private static final String keyTemplate = "%s.%s;aeron:udp?endpoint=%s:%d?streamId=%d";

	private static final Pattern KEY_PATTERN = Pattern.compile("(.*)\\.(\\w*);aeron:udp\\?endpoint=(\\w*):(\\d*)\\?streamId=(\\d*)");

	public static final String expires_pattern = "__keyevent@*__:expired";

	public static final String set_pattern = "__keyevent@*__:set";

	private Logger logger = LoggerFactory.getLogger(RedisDestinationRegistryClient.class);

	private RedisConnectionFactory redisConnectionFactory;

	private List<DestinationRegistrationListener> listeners = new LinkedList<>();

	private List<AeronChannelInformation> registrationWatch = new LinkedList<>();

	public RedisDestinationRegistryClient(RedisConnectionFactory redisConnectionFactory) {
		this.redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<AeronChannelInformation>(AeronChannelInformation.class));
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		this.redisConnectionFactory = redisConnectionFactory;

	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.redisTemplate.afterPropertiesSet();
		List<Topic> topics = new LinkedList<>();
	}

	@Override
	public void register(AeronChannelInformation channelInformation) {
		this.registrationWatch.add(channelInformation);
		store(channelInformation);
	}

	private void store(AeronChannelInformation channelInformation){
		String key = toRedisKey(channelInformation);
		redisTemplate.opsForValue().set(key,channelInformation);
	}

	@Override
	public List<AeronChannelInformation> locate(String destinationName) {
		Collection<String> keys = redisTemplate.keys(PREFIX+"."+destinationName+"*");
		List<AeronChannelInformation> destinations = new LinkedList<>();
		if(!CollectionUtils.isEmpty(keys)){
			destinations = redisTemplate.opsForValue().multiGet(keys);
		}
		return destinations;
	}

	@Override
	public void registerListerner(DestinationRegistrationListener listener) {
		this.listeners.add(listener);
	}

	@Override
	public void onMessage(Message message, byte[] pattern) {
		String key = new String(message.getBody());
		AeronChannelInformation information = fromRedisKey(key);
		String patternStr = new String(pattern);
		DestinationRegistrationEvent.RegistrationType registrationType = null;
		if(patternStr.equals(expires_pattern)){
			registrationType = DestinationRegistrationEvent.RegistrationType.OFFLINE;
		}else if(patternStr.equals(set_pattern)){
			registrationType = DestinationRegistrationEvent.RegistrationType.ONLINE;
		}
		DestinationRegistrationEvent event = new DestinationRegistrationEvent(information,registrationType);
		for(DestinationRegistrationListener  listener : listeners){
			if(key.startsWith(listener.getDestinationPattern())){
				listener.onEvent(event);
			}
		}
	}

	/**
	 * Sets the key on redis to keep it alive
	 */
	@Scheduled(fixedRate = 5000)
	public void ping(){
		logger.debug("Pinging server. {} configured destinations", registrationWatch.size());
		this.registrationWatch.forEach(this::store);
	}

	public static String toRedisKey(AeronChannelInformation information){
		return String.format(keyTemplate, PREFIX,information.getDestinationName(),information.getHost(),information.getPort(),information.getStreamId());
	}

	public static AeronChannelInformation fromRedisKey(String key){
		Matcher matcher = KEY_PATTERN.matcher(key);
		AeronChannelInformation information = null;
		if(matcher.matches()){
			information = AeronChannelInformation.newBuilder().destinationName(matcher.group(2))
					.host(matcher.group(3))
					.port(Integer.valueOf(matcher.group(4)))
					.streamId(Integer.valueOf(matcher.group(5))).build();
		}
		return information;
	}

}
