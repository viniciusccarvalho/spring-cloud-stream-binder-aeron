package org.springframework.cloud.stream.binder.aeron;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import io.aeron.Aeron;
import io.aeron.FragmentAssembler;
import io.aeron.Image;
import io.aeron.Subscription;
import org.agrona.concurrent.BackoffIdleStrategy;
import org.agrona.concurrent.IdleStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cloud.stream.binder.aeron.admin.DestinationRegistryClient;
import org.springframework.integration.endpoint.MessageProducerSupport;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

/**
 * @author Vinicius Carvalho
 */
public class AeronMessageContainer extends MessageProducerSupport {

	private Aeron aeron;
	final AtomicBoolean running = new AtomicBoolean(false);
	private Subscription subscription;
	private String uri;
	private Integer streamId;
	private Logger logger = LoggerFactory.getLogger(AeronMessageContainer.class);

	private ExecutorService threadPool = Executors.newFixedThreadPool(1);

	public AeronMessageContainer(Aeron aeron, String uri, Integer streamId) {
		this.aeron = aeron;
		this.uri = uri;
		this.streamId = streamId;
		logger.info("Creating Aeron container listening at : {}",uri);
	}

	@Override
	protected void doStart() {
		this.running.set(true);
		logger.info("Starting subscription on {} streamId: {}",this.uri,this.streamId);
		this.subscription = aeron.addSubscription(this.uri, this.streamId);
		this.threadPool.submit(new Worker());

	}

	@Override
	protected void doStop() {
		super.doStop();
	}


	class Worker implements Runnable {
		final IdleStrategy idleStrategy = new BackoffIdleStrategy(100, 10, TimeUnit.MICROSECONDS.toNanos(1), TimeUnit.MICROSECONDS.toNanos(100));
		@Override
		public void run() {
			try{
				boolean reachedEos = false;
				while(AeronMessageContainer.this.running.get()){
					final int fragmentsRead = AeronMessageContainer.this.subscription.poll(new FragmentAssembler((buffer, offset, length, header) -> {
						final byte[] data = new byte[length];
						buffer.getBytes(offset, data);
						Message<byte[]> message = MessageBuilder.withPayload(data)
								.setHeader("SESSION_ID",header.sessionId())
								.setHeader("STREAM_ID",header.streamId())
								.setHeader("TERM_ID",header.termId())
								.build();
						sendMessage(message);

					}), 20);
					if (0 == fragmentsRead)
					{
						if (!reachedEos && subscription.pollEndOfStreams(AeronMessageContainer::printEndOfStreamImage) > 0)
						{
							reachedEos = true;
						}
					}

					idleStrategy.idle(fragmentsRead);

				}
			}catch (Exception ex){
				ex.printStackTrace();
			}
		}
	}

	public static void printEndOfStreamImage(final Image image)
	{
		final Subscription subscription = image.subscription();
		System.out.println(String.format(
				"End Of Stream image on %s streamId=%d sessionId=%d from %s",
				subscription.channel(), subscription.streamId(), image.sessionId(), image.sourceIdentity()));
	}


}
