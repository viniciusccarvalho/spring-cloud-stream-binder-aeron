package org.springframework.cloud.stream.aeron;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;

/**
 * @author Vinicius Carvalho
 */
@SpringBootApplication
@EnableBinding(Sink.class)
public class AeronLogApplication {

	private Logger logger = LoggerFactory.getLogger(AeronLogApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AeronLogApplication.class,args);
	}

	@StreamListener(Sink.INPUT)
	public void onMessage(Message<?> message){
		logger.info("Received message : " + message.toString());
		logger.info("Contents: " + new String((byte[])message.getPayload()));
	}
}
