package cdr.cdr_service.Kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Kafka продюсер.
 */
@Service
public class KafkaProducer {
    /**
     * Шаблон Kafka.
     */
    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Создает экземпляр класса KafkaProducer с заданным шаблоном KafkaTemplate.
     *
     * @param kafkaTemplate шаблон KafkaTemplate для отправки сообщений
     */
    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Отправляет сообщение в указанную тему Kafka.
     *
     * @param message сообщение для отправки
     */
    public void sendMessage(String message) {
        kafkaTemplate.send("CDR-files", message);
    }

}
