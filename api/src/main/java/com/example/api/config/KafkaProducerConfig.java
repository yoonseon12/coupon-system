package com.example.api.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaProducerConfig {

	/**
	 * ProducerFactory 생성
	 */
	@Bean
	public ProducerFactory<String, Long> producerFactory() {
		Map<String, Object> config = new HashMap<>();

		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, LongSerializer.class);

		return new DefaultKafkaProducerFactory<>(config);
	}

	/**
	 * 카프카 토픽에 데이터를 전송하기 위해 사용할 카프카 템플릿
	 */
	@Bean
	public KafkaTemplate<String, Long> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}
}
