package com.learning.kafka.main;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.ValueMapper;
import org.apache.kafka.streams.StreamsBuilder;

import java.util.Arrays;
import java.util.Properties;

public class StreamsStarterApp {

	public static void main(String[] args) {

		Properties config = new Properties();
		config.put(StreamsConfig.APPLICATION_ID_CONFIG, "word-count-application");
		config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
		config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

		StreamsBuilder builder = new StreamsBuilder();

		KStream<String, String> wordCountInput = builder.stream("word-count-input");
		// Convert to lower case
		KTable<String, Long> wordCounts = wordCountInput.mapValues(value -> value.toLowerCase())
				// Convert to a map with null as key and String as value
				.flatMapValues(value -> Arrays.asList(value.split(" ")))
				// Modify the key and set it to value itself
				.selectKey((key, value) -> value)
				// Group By Key
				.groupByKey()
				// Count
				.count();

		wordCounts.toStream().to("word-count-output", Produced.with(Serdes.String(), Serdes.Long()));

		KafkaStreams streams = new KafkaStreams(builder.build(), config);
		streams.cleanUp(); // only do this in dev - not in prod
		streams.start();

		// print the topology
		System.out.println(streams.toString());

		// shutdown hook to correctly close the streams application
		Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

	}

}
