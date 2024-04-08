package cdr.cdr_service.CDRUtils;

import cdr.cdr_service.DAO.Models.Msisdns;

import java.util.List;
import java.util.Random;

/**
 * Класс, содержащий методы для генерации информации о звонках пользователей
 */
public class UserCallsTransactionsInfoGenerator {
    /**
     * Метод возвращает тип звонка:
     * <br> "01" - исходящий вызов,
     * <br> "02" - входящий вызов.
     * Вероятность выпадения любого = 0.5.
     *
     * @return строка с типом звонка.
     */
    public static String generateCallType() {
        return Math.random() < 0.5 ? "01" : "02";
    }

    /**
     * Метод генерирует номер телефона вызываемого абонента, делает он это следующим образом:
     * <br> с вероятностью 50 на 50 выбирается: осуществляется звонок абоненту внутренней сети или внешней,
     * далее, если внешней, то номер телефона составляется случайным образом, если внутренней, то
     * случайным образом выбирается номер телефона из базы абонентов сети. Также производится проверка на то,
     * не собирается и абонент позвонить самому себе.
     *
     * @return номер телефона вызываемого абонента.
     */
    public static String generateContactedMsisdn(List<Msisdns> msisdnsList, String phoneNumber) {
        StringBuilder contactedMsisdn = new StringBuilder();
        if (Math.random() < 0.5) {
            contactedMsisdn.append("7");
            contactedMsisdn.append((int) (Math.random() * 100 + 900));
            contactedMsisdn.append((int) (1000000 + Math.random() * 9000000));
        } else {
            Random rand = new Random();
            contactedMsisdn.append(msisdnsList.get(rand.nextInt(msisdnsList.size())).getPhoneNumber());
            while (contactedMsisdn.toString().equals(phoneNumber)) {
                contactedMsisdn.delete(0, contactedMsisdn.length());
                contactedMsisdn.append(msisdnsList.get(rand.nextInt(msisdnsList.size())).getPhoneNumber());
            }
        }
        return contactedMsisdn.toString();
    }

    /**
     * Метод возвращает время конца звонка, сдвинутое относительно начала на случайное время от 1 до 10 минут.
     *
     * @param callStartTime время начала звонка (Unix time seconds).
     * @return время конца звонка (Unix time seconds).
     */
    public static long generateCallEndTime(Long callStartTime) {
        return callStartTime + (long) (Math.random() * (540) + 60);
    }
}
