package cdr.cdr_service.CDRUtils;

public class UserCallsTransactionsInfoGenerator {
    private static final String SERVICED_PHONE_NUMBER_CODE = "921";

    public static String generateCallType() {
        int number = (int) (Math.random() * 2 + 1);
        return "0" + number;
    }

    public static String generateContactedMsisdn() {
        StringBuilder contactedMsisdn = new StringBuilder();
        contactedMsisdn.append("7");
        boolean isServiced = Math.random() < 0.5;
        if (!isServiced) {
            int code = (int) (Math.random() * 100 + 900);
            contactedMsisdn.append(code == 921 ? code++ : code);
        } else {
            contactedMsisdn.append(SERVICED_PHONE_NUMBER_CODE);
        }
        contactedMsisdn.append((int) (1000000 + Math.random() * 9000000));
        return contactedMsisdn.toString();
    }

    public static long generateCallEndTime(Long callStartTime) {
        long randomTimeSeconds = (long) (Math.random() * (540) + 60);

        return callStartTime + randomTimeSeconds;
    }
}
