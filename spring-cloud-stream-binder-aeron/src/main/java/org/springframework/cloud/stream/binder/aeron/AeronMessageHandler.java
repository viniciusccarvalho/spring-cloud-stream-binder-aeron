package org.springframework.cloud.stream.binder.aeron;


import io.aeron.Aeron;
import io.aeron.Publication;
import org.agrona.BufferUtil;
import org.agrona.concurrent.UnsafeBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cloud.stream.binder.aeron.provisioning.AeronProducerDestination;
import org.springframework.integration.handler.AbstractMessageHandler;
import org.springframework.messaging.Message;

/**
 * @author Vinicius Carvalho
 */
public class AeronMessageHandler extends AbstractMessageHandler {

	private Publication publication;

	private AeronProducerDestination aeronProducerDestination;

	private Logger logger = LoggerFactory.getLogger(AeronMessageHandler.class);

	private Aeron aeron;

	public AeronMessageHandler(AeronProducerDestination aeronProducerDestination, Aeron aeron) {
		this.aeronProducerDestination = aeronProducerDestination;
		this.aeron = aeron;
	}

	@Override
	protected void onInit() throws Exception {
		logger.info("Binding Control port at : {}",this.aeronProducerDestination.getName());
		this.publication = this.aeron.addPublication(this.aeronProducerDestination.getName(),this.aeronProducerDestination.getStreamId());
		for(String destination : aeronProducerDestination.getDestinations()){
			logger.info("Adding destination: {}",destination);
			this.publication.addDestination(destination);
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

}
