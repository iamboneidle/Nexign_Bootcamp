package cdr.cdr_service.CDRUtils;

import cdr.cdr_service.DAO.Models.Msisdns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Этот класс представляет собой поток, генерирующий транзакции пользователя за день. Я сделал так,
 * что пользователь за день звони один раз.
 */
public class User implements Runnable {
    /**
     * Номер телефона абонента.
     */
    private final String phoneNumber;
    /**
     * Список объектов Msisdn.
     */
    private final List<Msisdns> msisdnsList;
    /**
     * Демон поток, генерирующий новую дату фоном.
     */
    private final DateGenerator daemonThread;
    /**
     * Конкурентная очередь, в которую отправляются транзакции абонента за день.
     */
    private final ConcurrentQueue concurrentQueue;
    /**
     * Логгер, выводящий уведомление об ошибке.
     */
    private static final Logger LOGGER = Logger.getLogger(User.class.getName());
    /**
     * Вероятность того, что в конкретный день пользователь совершит звонок.
     */
    private static final float USER_MAKES_CALL_TODAY_PROBABILITY = 0.3f;

    /**
     * Конструктор класса.
     *
     * @param phoneNumber Номер телефона абонента, в роли которого выступает поток.
     * @param msisdnsList Список всех абонентов.
     * @param daemonThread Демон поток, генерирующий даты.
     * @param concurrentQueue Конкурентная очередь.
     */
    public User(String phoneNumber, List<Msisdns> msisdnsList, DateGenerator daemonThread, ConcurrentQueue concurrentQueue) {
        this.phoneNumber = phoneNumber;
        this.msisdnsList = msisdnsList;
        this.daemonThread = daemonThread;
        this.concurrentQueue = concurrentQueue;
    }

    /**
     * Перегруженный метод с таской потоку. Он синхронизирован с демон потоком, с которого получаем от него дату нового дня, проверяем curMsisdnQuantity и
     * msisdnsList.size() на предмет добавления нового пользователя, если пользователь был добавлен, то добавляем его номер телефона
     * в список, по которому можем генерировать звонки, получаем новую дату, вызываем метод генерации транзакций, добавляем транзакции
     * в конкурентную очередь и чистим список транзакций на день.
     */
    @Override
    public void run() {
        List<TransactionObject> transactionObjectsForDay = new ArrayList<>();
        List<String> phoneNumbersList = new ArrayList<>();
        msisdnsList.forEach(msisdn -> phoneNumbersList.add(msisdn.getPhoneNumber()));
        int curMsisdnQuantity = msisdnsList.size();
        while (true) {
            synchronized (daemonThread) {
                try {
                    daemonThread.wait();
                } catch (InterruptedException e) {
                    LOGGER.log(Level.SEVERE, "EXCEPTION: " + Arrays.toString(e.getStackTrace()));
                    throw new RuntimeException(e);
                }
                if (curMsisdnQuantity != msisdnsList.size()) {
                    curMsisdnQuantity = msisdnsList.size();
                    phoneNumbersList.add(msisdnsList.get(msisdnsList.size() - 1).getPhoneNumber());
                }
                if (Math.random() <= USER_MAKES_CALL_TODAY_PROBABILITY) {
                    String curDate = daemonThread.getCurrentDate();
                    transactionMaker(phoneNumbersList, transactionObjectsForDay, curDate);
                    concurrentQueue.enqueue(transactionObjectsForDay);
                    transactionObjectsForDay.clear();
                }
            }
        }
    }

    /**
     * Метод, генерирующий транзакции. Получаем временной промежуток звонка, проверяем его на null, генерируем номер телефона абонента,
     * которому звоним, если номер этого абонента является обслуживаемым, то генерируем транзакцию для него тоже, если нет, то просто
     * генерируем транзакцию для звонившего.
     *
     * @param phoneNumbersList Список обслуживаемых номеров.
     * @param transactionObjectsForDay Список транзакций на день.
     * @param curDate Сегодняшняя дата.
     */
    private void transactionMaker(List<String> phoneNumbersList, List<TransactionObject> transactionObjectsForDay, String curDate) {
        Long[] callTimeGap = UserCallsTransactionsInfoGenerator.generateCallTimeGap(curDate);
        if (callTimeGap != null) {
            String contactedPhoneNumber = UserCallsTransactionsInfoGenerator.generateContactedMsisdn(msisdnsList, phoneNumber);
            if (phoneNumbersList.contains(contactedPhoneNumber)) {
                String callTypeForContacting = UserCallsTransactionsInfoGenerator.generateCallType();
                String callTypeForContacted = callTypeForContacting.equals("01") ? "02" : "01";
                transactionAdder(transactionObjectsForDay, callTypeForContacting, phoneNumber, contactedPhoneNumber, callTimeGap);
                transactionAdder(transactionObjectsForDay, callTypeForContacted, contactedPhoneNumber, phoneNumber, callTimeGap);
            } else {
                transactionAdder(transactionObjectsForDay, UserCallsTransactionsInfoGenerator.generateCallType(), phoneNumber, contactedPhoneNumber, callTimeGap);
            }
        }
    }

    /**
     * Метод, добавляющий транзакцию в список транзакций абонента за день.
     *
     * @param transactionObjectsForDay Список транзакций за день.
     * @param callType Тип совершенного звонка.
     * @param phoneKeeper Звонивший абонент.
     * @param contacted Контактируемый абонент.
     * @param callTimeGap Время, в которое проходил звонок.
     */
    private void transactionAdder(List<TransactionObject> transactionObjectsForDay, String callType,
                                  String phoneKeeper, String contacted, Long[] callTimeGap) {
        transactionObjectsForDay.add(
                new TransactionObject(
                        callType,
                        phoneKeeper,
                        contacted,
                        callTimeGap[0],
                        callTimeGap[1]
                )
        );
    }
}
