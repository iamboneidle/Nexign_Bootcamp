package hrs.hrs_service.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hrs.hrs_service.DAO.Repository.RatesRepository;
import hrs.hrs_service.HRSUtils.CallReceipt;
import hrs.hrs_service.HRSUtils.DataToPay;
import hrs.hrs_service.HRSUtils.ReceiptMaker;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Сервис отправляющий данный на расчет и чек по данным звонка в BRT.
 */
@Service
public class HRSService {
    /**
     * Репозиторий для сущности Rates в Redis.
     */
    @Autowired
    private RatesRepository ratesRepository;
    /**
     * Сервис по отправке чеков в BRT.
     */
    @Autowired
    private CallReceiptSenderService callReceiptSenderService;
    /**
     * Класс, составляющий чеки.
     */
    @Autowired
    private ReceiptMaker receiptMaker;
    /**
     * Объект ObjectMapper для преобразования в Json объекта.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();
    /**
     * Логгер для выводы уведомлений.
     */
    private static final Logger LOGGER = Logger.getLogger(HRSService.class.getName());

    /**
     * Метод, который отправляет данные на расчет, получает чека и передает его на отправку.
     * Проверяет, не равен ли объект CallReceipt null.
     *
     * @param dataToPay Объект с данными о звонке.
     */
    public void makeAndSendCallReceipt(DataToPay dataToPay) {
        try {
            CallReceipt callReceipt = receiptMaker.makeCalculation(dataToPay);
            if (callReceipt != null) {
                String json = objectMapper.writeValueAsString(callReceipt);
                callReceiptSenderService.sendCallReceipt(json);
            }
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.SEVERE, "EXCEPTION: " + Arrays.toString(e.getStackTrace()));
        }
    }

    /**
     * Метод очищающий Redis при запуске сервиса.
     */
    @PostConstruct
    private void clearRedis() {
        ratesRepository.deleteAll();
    }
}
