package cdr.cdr_service.CDRUtils;

import cdr.cdr_service.DAO.Models.Msisdns;
import cdr.cdr_service.DAO.Models.Transactions;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/**
 * Этот класс представляет собой поток, генерирующий транзакции пользователя за месяц.
 */
public class CDRUser implements Callable<List<TransactionObject>> {
    /**
     * Массив количества дней в каждом месяце.
     * <br> 0-й элемент - Январь
     * <br> 11-й элемент - Декабрь
     */
    public static final int[] DAYS_IN_MONTH_QUANTITY = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    /**
     * Номер телефона абонента.
     */
    private final String phoneNumber;
    /**
     * Номер месяца, за который генерируются транзакции
     */
    private final int monthNum;
    /**
     * Список объектов Msisdn.
     */
    private final List<Msisdns> msisdnsList;
    /**
     * Счетчик, синхронизирующий выполнение потоков.
     */
    private final CountDownLatch latch;

    /**
     * Конструктор.
     *
     * @param phoneNumber Номер телефона абонента.
     * @param monthNum    Номер месяца, за который генерируются транзакции.
     * @param latch       Счетчик, синхронизирующий выполнение потоков.
     * @param msisdnsList Список объектов Msisdn.
     */
    public CDRUser(String phoneNumber, int monthNum, CountDownLatch latch, List<Msisdns> msisdnsList) {
        this.phoneNumber = phoneNumber;
        this.monthNum = monthNum;
        this.msisdnsList = msisdnsList;
        this.latch = latch;
    }

    /**
     * Генерирует транзакции для указанного абонента за указанный месяц.
     *
     * @return Список транзакций для указанного абонента за указанный месяц.
     */
    @Override
    public List<TransactionObject> call() {
        List<TransactionObject> transactionObjectsForMonth = new ArrayList<>();
        List<String> phoneNumbersList = new ArrayList<>();
        msisdnsList.forEach(msisdn -> phoneNumbersList.add(msisdn.getPhoneNumber()));
        long currentTime = Instant.now().getEpochSecond() + (long) (monthNum - 1) * DAYS_IN_MONTH_QUANTITY[monthNum - 1] * 3600 * 24;
        int callsInMonthQuantity = (int) (Math.random() * 50 + 1);
        long periodBetweenCalls = ((long) DAYS_IN_MONTH_QUANTITY[monthNum - 1] * 3600 * 24) / callsInMonthQuantity;
        long endOfMonthTime = Instant.now().getEpochSecond() + (long) (monthNum) * DAYS_IN_MONTH_QUANTITY[monthNum - 1] * 3600 * 24;

        for (long cur = currentTime; cur <= endOfMonthTime; cur += periodBetweenCalls) {
            addHelper(phoneNumbersList, cur, transactionObjectsForMonth);
        }
        transactionObjectsForMonth.sort(Comparator.comparingLong(TransactionObject::getCallStartTime));
        latch.countDown();
        return transactionObjectsForMonth;
    }

    /**
     * Добавляет транзакцию для указанного времени в список транзакций для месяца.
     *
     * @param phoneNumbersList           Список номеров телефонов абонентов.
     * @param cur                        Текущее время.
     * @param transactionObjectsForMonth Список транзакций для месяца.
     */
    public void addHelper(List<String> phoneNumbersList, long cur, List<TransactionObject> transactionObjectsForMonth) {
        String contactedPhoneNumber = UserCallsTransactionsInfoGenerator.generateContactedMsisdn(msisdnsList, phoneNumber);
        if (phoneNumbersList.contains(contactedPhoneNumber)) {
            String callTypeForContacting = UserCallsTransactionsInfoGenerator.generateCallType();
            String callTypeForContacted = callTypeForContacting.equals("01") ? "02" : "01";
            long callEndTime = UserCallsTransactionsInfoGenerator.generateCallEndTime(cur);
            transactionObjectsForMonth.add(
                    new TransactionObject(callTypeForContacting,
                            phoneNumber,
                            contactedPhoneNumber,
                            cur,
                            callEndTime
                    )
            );
            transactionObjectsForMonth.add(
                    new TransactionObject(callTypeForContacted,
                            contactedPhoneNumber,
                            phoneNumber,
                            cur,
                            callEndTime
                    )
            );
        } else {
            transactionObjectsForMonth.add(
                    new TransactionObject(UserCallsTransactionsInfoGenerator.generateCallType(),
                            phoneNumber,
                            contactedPhoneNumber,
                            cur,
                            UserCallsTransactionsInfoGenerator.generateCallEndTime(cur))
            );
        }
    }
}
