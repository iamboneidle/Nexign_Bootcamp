package brt.brt_service.Kafka;

import org.springframework.beans.factory.annotation.Value;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс конфигурации Apache Kafka.
 */
@Configuration
@EnableKafka
public class KafkaConfig {

    /**
     * Сервер для подключения.
     */
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * Конфигурация для настройки параметров Kafka консьюмера.
     *
     * @return ConsumerFactory.
     */
    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapServers);
        return new DefaultKafkaConsumerFactory<>(props);
    }
}
