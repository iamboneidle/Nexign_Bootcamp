package brt.brt_service.Kafka;

import brt.brt_service.BRTUtils.DataFromKafka;
import brt.brt_service.Services.BRTService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Кафка консьюмер.
 */
@Component
public class KafkaConsumer {
    /**
     *
     */
    @Autowired
    private BRTService brtService;
    /**
     * Объект для преобразования Json в объект класса DataFromKafka.
     */
    private final Gson gson = new Gson();
    /**
     * Логгер для вывода уведомлений.
     */
    private static final Logger LOGGER = Logger.getLogger(KafkaConsumer.class.getName());

    /**
     * Метод, забирающий из Кафки сообщения от CDR.
     *
     * @param message Сообщение.
     */
    @KafkaListener(topics = "CDR-files", groupId = "my-group")
    public void flightEventConsumer(String message) {
        DataFromKafka dataFromKafka = gson.fromJson(message, DataFromKafka.class);
        LOGGER.log(Level.INFO, "OK: received " + dataFromKafka.getFileName() + " from Kafka");
        brtService.handleCDRFile(dataFromKafka.getFileContent());
    }
}
