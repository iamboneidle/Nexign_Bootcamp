package cdr.cdr_service.Kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Конфигурация для работы с Apache Kafka.
 */
@Configuration
public class KafkaConfig {
    /**
     * Список серверов Kafka для подключения.
     */
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * Конфигурация для настройки параметров продюсера Kafka.
     *
     * @return Настройки продюсера Kafka.
     */
    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return props;
    }

    /**
     * Создает новую тему Kafka для обработки CDR-файлов.
     *
     * @return Новый топик Kafka.
     */
    @Bean
    public NewTopic newTopic() {
        return new NewTopic("CDR-files", 1, (short) 1);
    }
}
