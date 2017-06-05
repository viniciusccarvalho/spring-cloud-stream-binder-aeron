package org.springframework.cloud.stream.aeron;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Vinicius Carvalho
 */
@SpringBootApplication
@EnableBinding(Source.class)
@RestController
public class AeronHttpApplication {

	@Autowired
	private Source source;


	@RequestMapping(method = RequestMethod.POST)
	public void sendMessage(@RequestBody String payload){
		source.output().send(MessageBuilder.withPayload(payload).build());
	}

	public static void main(String[] args) {
		SpringApplication.run(AeronHttpApplication.class,args);
	}
}
