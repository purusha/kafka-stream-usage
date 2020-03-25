package org.at.tools;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;

public abstract class ShutdownHelper {
	
	public void waitSigint(Consumer<StreamsBuilder> main) {
		final Properties props = new Properties();
		props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-" + this.getClass().getSimpleName().toLowerCase());
		props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		
		final StreamsBuilder builder = new StreamsBuilder();
		
		main.accept(builder);
		
		final Topology topology = builder.build();
		System.out.println(topology.describe());

		final KafkaStreams streams = new KafkaStreams(topology, props);
		
		streams.setUncaughtExceptionHandler((Thread t, Throwable e) -> {
			System.out.println("Exception on thread " + t.getName() + " :");
			System.out.println(e.getStackTrace());
		});
		
		final CountDownLatch latch = new CountDownLatch(1);

		// attach shutdown handler to catch control-c
		Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
			@Override
			public void run() {
				System.out.println("received SININT :(");
				streams.close();
				latch.countDown();
			}
		});

		try {
			streams.start();
			latch.await();
		} catch (Throwable e) {
			System.exit(1);
		}

		System.exit(0);		
	}
	
}
