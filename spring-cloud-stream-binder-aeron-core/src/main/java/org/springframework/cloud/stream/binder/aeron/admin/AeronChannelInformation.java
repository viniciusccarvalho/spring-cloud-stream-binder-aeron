package org.springframework.cloud.stream.binder.aeron.admin;

/**
 * @author Vinicius Carvalho
 */
public class AeronChannelInformation {

	private Integer port;

	private String host;

	private Integer streamId = 1;

	private String destinationName;

	AeronChannelInformation() {
	}

	public Integer getPort() {
		return port;
	}

	public String getHost() {
		return host;
	}

	public Integer getStreamId() {
		return streamId;
	}

	public String getDestinationName() {
		return destinationName;
	}

	public class AeronChannelInformationBuilder {
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
			return information;
		}

	}
}
