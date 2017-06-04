package org.springframework.cloud.stream.binder.aeron.admin;

/**
 * @author Vinicius Carvalho
 */
public class DestinationRegistrationEvent {
	private AeronChannelInformation information;
	private RegistrationType registrationType;

	public AeronChannelInformation getInformation() {
		return information;
	}

	public void setInformation(AeronChannelInformation information) {
		this.information = information;
	}

	public RegistrationType getRegistrationType() {
		return registrationType;
	}

	public void setRegistrationType(RegistrationType registrationType) {
		this.registrationType = registrationType;
	}

	public static enum RegistrationType {
		ONLINE,OFFLINE;
	}
}
