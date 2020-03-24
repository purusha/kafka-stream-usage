package org.at;

import java.util.Arrays;
import java.util.Locale;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;
import org.at.tools.ShutdownHelper;

public class WordCount extends ShutdownHelper implements Runnable {	
	@Override
	public void run() {
		
		waitSigint(builder -> {
			final KStream<String, String> source = builder.stream("streams-plaintext-input");

			source
				.flatMapValues(value -> Arrays.asList(value.toLowerCase(Locale.getDefault()).split("\\W+")))
				.groupBy((key, value) -> value)
				.count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as("counts-store")).toStream()
				.to("streams-wordcount-output", Produced.with(Serdes.String(), Serdes.Long()));
		});
		
	}
}
