package cdr.cdr_service.CDRUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class CDRUser implements Callable<List<TransactionObject>> {
    public static final int[] DAYS_IN_MONTH_QUANTITY = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private final String msisdn;
    private final int monthNum;
    private CountDownLatch latch;

    public CDRUser(String msisdn, int monthNum, CountDownLatch latch) {
        this.msisdn = msisdn;
        this.monthNum = monthNum;
        this.latch = latch;
    }

    @Override
    public List<TransactionObject> call() {
        List<TransactionObject> transactionObjectsForMonth = new ArrayList<>();
        long currentTime = Instant.now().getEpochSecond() + (long) (monthNum - 1) * DAYS_IN_MONTH_QUANTITY[monthNum - 1] * 3600 * 24;
        int callsInMonthQuantity = (int) (Math.random() * 50 + 1);
        long periodBetweenCalls =  ((long) DAYS_IN_MONTH_QUANTITY[monthNum - 1] * 3600 * 24) / callsInMonthQuantity;
        long endOfMonthTime = Instant.now().getEpochSecond() + (long) (monthNum) * DAYS_IN_MONTH_QUANTITY[monthNum - 1] * 3600 * 24;

        for (long cur = currentTime; cur <= endOfMonthTime; cur += periodBetweenCalls) {
            String contactedMsisdn = UserCallsTransactionsInfoGenerator.generateContactedMsisdn();

            while(contactedMsisdn.equals(msisdn)) {
                contactedMsisdn = UserCallsTransactionsInfoGenerator.generateContactedMsisdn();
            }
            transactionObjectsForMonth.add(
                    new TransactionObject(UserCallsTransactionsInfoGenerator.generateCallType(),
                            msisdn,
                            contactedMsisdn,
                            cur,
                            UserCallsTransactionsInfoGenerator.generateCallEndTime(cur))
            );
        }
        transactionObjectsForMonth.sort(Comparator.comparingLong(TransactionObject::getCallStartTime));
        latch.countDown();
        return transactionObjectsForMonth;
    }
}
