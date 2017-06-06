package org.springframework.cloud.stream.binder.aeron.admin;

/**
 * @author Vinicius Carvalho
 */
public class DestinationRegistrationEvent {
	private AeronChannelInformation information;

	private RegistrationType registrationType;

	public DestinationRegistrationEvent(){}

	public DestinationRegistrationEvent(AeronChannelInformation information, RegistrationType registrationType) {
		this.information = information;
		this.registrationType = registrationType;
	}

	public AeronChannelInformation getInformation() {
		return this.information;
	}

	public void setInformation(AeronChannelInformation information) {
		this.information = information;
	}

	public RegistrationType getRegistrationType() {
		return this.registrationType;
	}

	public void setRegistrationType(RegistrationType registrationType) {
		this.registrationType = registrationType;
	}

	public static enum RegistrationType {
		ONLINE,OFFLINE;
	}
}
