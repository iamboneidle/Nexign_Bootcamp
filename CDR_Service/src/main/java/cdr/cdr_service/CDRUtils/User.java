package cdr.cdr_service.CDRUtils;

import cdr.cdr_service.DAO.Models.Msisdns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Этот класс представляет собой поток, генерирующий транзакции пользователя за месяц.
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
    private final DateGenerator daemonThread;
    private final ConcurrentQueue concurrentQueue;
    private static final Logger LOGGER = Logger.getLogger(User.class.getName());
    private static final float USER_MAKES_CALL_TODAY_PROBABILITY = 0.3f;

    public User(String phoneNumber, List<Msisdns> msisdnsList, DateGenerator daemonThread, ConcurrentQueue concurrentQueue) {
        this.phoneNumber = phoneNumber;
        this.msisdnsList = msisdnsList;
        this.daemonThread = daemonThread;
        this.concurrentQueue = concurrentQueue;
    }

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
