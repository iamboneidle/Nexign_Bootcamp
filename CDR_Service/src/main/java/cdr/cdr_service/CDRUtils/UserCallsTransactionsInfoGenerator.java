package cdr.cdr_service.CDRUtils;

import cdr.cdr_service.DAO.Models.Msisdns;
import jakarta.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс, содержащий методы для генерации информации о звонках пользователей
 */
public class UserCallsTransactionsInfoGenerator {
    /**
     * Рандом.
     */
    private static final Random RANDOM = new Random();
    /**
     * Логгер, выводящий ошибки.
     */
    private static final Logger LOGGER = Logger.getLogger(UserCallsTransactionsInfoGenerator.class.getName());
    /**
     * Время, когда мы считаем, что новые сутки начались.
     */
    private static final String DAY_START_TIME = "00:00:00";
    /**
     * Время, когда мы считаем, что новые сутки закончились.
     */
    private static final String DAY_END_TIME = "23:59:59";
    /**
     * Вероятность того, что звонок будет исходящим.
     */
    private static final float OUTCOMING_CALL_PROBABILITY = 0.5f;
    /**
     * Минимальное время длительности звонка в секундах.
     */
    private static final int CALL_DURATION_BOTTOM_BORDER = 10;
    /**
     * Максимальное время длительности звонка в секундах.
     */
    private static final int CALL_DURATION_TOP_BORDER = 590;
    /**
     * Множитель, чтобы перевести миллисекунды в секунды.
     */
    private static final int MILLISECONDS_TO_SECONDS = 1000;

    /**
     * Метод возвращает тип звонка:
     * <br> "01" - исходящий вызов,
     * <br> "02" - входящий вызов.
     * Вероятность выпадения любого = 0.5.
     *
     * @return Строка с типом звонка.
     */
    public static String generateCallType() {
        return Math.random() < OUTCOMING_CALL_PROBABILITY ? "01" : "02";
    }

    /**
     * Метод генерирует номер телефона вызываемого абонента, делает он это следующим образом:
     * <br> с вероятностью 50 на 50 выбирается: осуществляется звонок абоненту внутренней сети или внешней,
     * далее, если внешней, то номер телефона составляется случайным образом, если внутренней, то
     * случайным образом выбирается номер телефона из базы абонентов сети. Также производится проверка на то,
     * не собирается и абонент позвонить самому себе.
     *
     * @return Номер телефона вызываемого абонента.
     */
    public static String generateContactedMsisdn(List<Msisdns> msisdnsList, String phoneNumber) {
        StringBuilder contactedMsisdn = new StringBuilder();
        if (Math.random() < 0.5) {
            return generateNotServicedMsisdn();
        } else {
            contactedMsisdn.append(msisdnsList.get(RANDOM.nextInt(msisdnsList.size())).getPhoneNumber());
            while (contactedMsisdn.toString().equals(phoneNumber)) {
                contactedMsisdn.delete(0, contactedMsisdn.length());
                contactedMsisdn.append(msisdnsList.get(RANDOM.nextInt(msisdnsList.size())).getPhoneNumber());
            }
            return contactedMsisdn.toString();
        }
    }

    /**
     * Метод генерирует номера телефонов необслуживаемых абонентов.
     *
     * @return Номер телефона необслуживаемого абонента.
     */
    public static String generateNotServicedMsisdn() {
        return "7" +
                (int) (Math.random() * 100 + 900) +
                (int) (1000000 + Math.random() * 9000000);
    }

    /**
     * Метод, генерирующий промежуток, в который происходит звонок. Сначала получаем время в Unix time seconds
     * когда день начинается и когда заканчивается, а потом в этом промежутке генерируем время нашего звонка в
     * Unix time seconds.
     *
     * @param callDate Дата, когда делается звонок.
     * @return Массив из двух значений, когда начинается и заканчивается звонок.
     */
    @Nullable
    public static Long[] generateCallTimeGap(String callDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        Long[] callTimeGap = new Long[2];

        try {
            long startBorder = sdf.parse(callDate + " " + DAY_START_TIME).getTime() / MILLISECONDS_TO_SECONDS;
            long endBorder = sdf.parse(callDate + " " + DAY_END_TIME).getTime() / MILLISECONDS_TO_SECONDS;
            long callTimeStart = (long) (startBorder + (endBorder - startBorder) * Math.random());
            callTimeGap[0] = callTimeStart;
            long callTimeEnd = (long) (callTimeStart + Math.random() * CALL_DURATION_TOP_BORDER + CALL_DURATION_BOTTOM_BORDER);
            callTimeGap[1] = callTimeEnd;

            return callTimeGap;
        } catch (ParseException e) {
            LOGGER.log(Level.SEVERE, "EXCEPTION: " + Arrays.toString(e.getStackTrace()));
        }

        return null;
    }
}
