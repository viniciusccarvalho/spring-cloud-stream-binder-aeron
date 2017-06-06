package org.springframework.cloud.stream.binder.aeron.admin;

/**
 * @author Vinicius Carvalho
 */
public class AeronUtils {

	private static final String consumerConnection = "aeron:udp?endpoint=%s:%d";

	private static final String controlConnection = "aeron:udp?control=%s:%d|control-mode=manual";

	public static String controlConnnectionString(String host, Integer port){
		return String.format(controlConnection, host,port);
	}

	public static String consumerConnectionString(String host, Integer port){
		return String.format(consumerConnection, host,port);
	}
}
