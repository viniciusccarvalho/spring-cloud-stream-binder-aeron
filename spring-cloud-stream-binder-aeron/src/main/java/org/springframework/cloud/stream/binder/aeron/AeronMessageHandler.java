package org.springframework.cloud.stream.binder.aeron;


import java.util.HashSet;
import java.util.Set;

import io.aeron.Aeron;
import io.aeron.Publication;
import org.agrona.BufferUtil;
import org.agrona.concurrent.UnsafeBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cloud.stream.binder.aeron.admin.AeronChannelInformation;
import org.springframework.cloud.stream.binder.aeron.admin.DestinationRegistrationEvent;
import org.springframework.cloud.stream.binder.aeron.admin.DestinationRegistrationListener;
import org.springframework.cloud.stream.binder.aeron.provisioning.AeronProducerDestination;
import org.springframework.integration.handler.AbstractMessageHandler;
import org.springframework.messaging.Message;

/**
 * @author Vinicius Carvalho
 */
public class AeronMessageHandler extends AbstractMessageHandler implements DestinationRegistrationListener{

	private Publication publication;

	private AeronProducerDestination aeronProducerDestination;

	private Logger logger = LoggerFactory.getLogger(AeronMessageHandler.class);

	private Aeron aeron;

	private Set<AeronChannelInformation> destinations = new HashSet<>();

	private final String destinationName;

	public AeronMessageHandler(AeronProducerDestination aeronProducerDestination, Aeron aeron) {
		this.aeronProducerDestination = aeronProducerDestination;
		this.aeron = aeron;
		this.destinationName ="aeron.destinationName."+aeronProducerDestination.getChannelName();
	}

	@Override
	protected void onInit() throws Exception {
		logger.info("Binding Control port at : {}",this.aeronProducerDestination.getName());
		this.publication = this.aeron.addPublication(this.aeronProducerDestination.getName(),this.aeronProducerDestination.getStreamId());
		for(String destination : aeronProducerDestination.getDestinations()){
			logger.debug("Adding destinationName: {}",destination);
			this.publication.addDestination(destination);
		}
		for(AeronChannelInformation information : this.destinations){
			this.publication.addDestination(String.format("aeron:udp?endpoint=%s:%d", information.getHost(),information.getPort()));
		}
	}

	@Override
	protected void handleMessageInternal(Message<?> message) throws Exception {
		byte[] payload = (byte[]) message.getPayload();
		UnsafeBuffer buffer = new UnsafeBuffer(BufferUtil.allocateDirectAligned(payload.length, 64));
		buffer.putBytes(0,payload);
		long result = publication.offer(buffer);
		if (result < 0L)
		{
			if (result == Publication.BACK_PRESSURED)
			{
				logger.error("Offer failed due to back pressure");
			}
			else if (result == Publication.NOT_CONNECTED)
			{
				logger.error("Offer failed because publisher is not connected to subscriber");
			}
			else if (result == Publication.ADMIN_ACTION)
			{
				logger.error("Offer failed because of an administration action in the system");
			}
			else if (result == Publication.CLOSED)
			{
				logger.error("Offer failed publication is closed");
			}
			else
			{
				logger.error("Offer failed due to unknown reason");
			}
		}
		else
		{
			logger.info("Message published, stream at pos: {}",result);
		}
	}

	public void addDestination(AeronChannelInformation information){
		this.destinations.add(information);
	}

	@Override
	public void onEvent(DestinationRegistrationEvent event) {
		logger.debug("Event: {} for {}",event.getRegistrationType(),event.getInformation());
		String endpoint = String.format("aeron:udp?endpoint=%s:%d", event.getInformation().getHost(),event.getInformation().getPort());
		switch (event.getRegistrationType()){
			case ONLINE:
				if(!this.destinations.contains(event.getInformation())) {
					this.publication.addDestination(endpoint);
					logger.debug("Detected new subscriber for {}. Registering endpoint {}",event.getInformation().getDestinationName(),endpoint);
					this.destinations.add(event.getInformation());
				}
				break;
			case OFFLINE:
				logger.debug("Removing destinationName : {}",endpoint);
				this.destinations.remove(event.getInformation());
				this.publication.removeDestination(endpoint);
		}
	}

	@Override
	public String getDestinationPattern() {
		return destinationName;
	}
}
