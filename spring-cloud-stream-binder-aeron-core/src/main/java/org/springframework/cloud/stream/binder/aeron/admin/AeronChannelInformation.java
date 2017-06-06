package org.springframework.cloud.stream.binder.aeron.admin;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

/**
 * @author Vinicius Carvalho
 */
@JsonDeserialize(builder = AeronChannelInformation.AeronChannelInformationBuilder.class)
public class AeronChannelInformation {

	private Integer port = -1;

	private String host = "localhost";

	private Integer streamId = 1;

	private String destinationName = "";

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		AeronChannelInformation that = (AeronChannelInformation) o;

		if (!port.equals(that.port)) return false;
		if (!host.equals(that.host)) return false;
		if (!streamId.equals(that.streamId)) return false;
		return destinationName.equals(that.destinationName);
	}

	@Override
	public int hashCode() {
		int result = port.hashCode();
		result = 31 * result + host.hashCode();
		result = 31 * result + streamId.hashCode();
		result = 31 * result + destinationName.hashCode();
		return result;
	}

	AeronChannelInformation() {
	}

	public static AeronChannelInformationBuilder newBuilder(){
		return new AeronChannelInformationBuilder();
	}

	public Integer getPort() {
		return this.port;
	}

	public String getHost() {
		return this.host;
	}

	public Integer getStreamId() {
		return this.streamId;
	}

	public String getDestinationName() {
		return this.destinationName;
	}

	@JsonPOJOBuilder(withPrefix = "")
	public static class AeronChannelInformationBuilder {
		private AeronChannelInformation information = new AeronChannelInformation();

		public AeronChannelInformationBuilder host(String host){
			this.information.host = host;
			return this;
		}

		public AeronChannelInformationBuilder port(Integer port){
			this.information.port = port;
			return this;
		}

		public AeronChannelInformationBuilder destination(String destination){
			this.information.destinationName = destination;
			return this;
		}

		public AeronChannelInformationBuilder streamId(Integer streamId){
			this.information.streamId = streamId;
			return this;
		}

		public AeronChannelInformation build(){
			return this.information;
		}

	}
}
