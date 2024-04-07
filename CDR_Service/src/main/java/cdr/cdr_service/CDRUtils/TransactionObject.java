package cdr.cdr_service.CDRUtils;

import lombok.Getter;

import java.util.StringJoiner;

@Getter
public class TransactionObject {
    private final String callType;
    private final String servicedMsisdn;
    private final String contactedMsisdn;
    private final long callStartTime;
    private final long callEndTime;

    TransactionObject(String callType, String servicedMsisdn, String contactedMsisdn, long callStartTime, long callEndTime) {
        this.callType = callType;
        this.servicedMsisdn = servicedMsisdn;
        this.contactedMsisdn = contactedMsisdn;
        this.callStartTime = callStartTime;
        this.callEndTime = callEndTime;
    }

    @Override
    public String toString() {
        StringJoiner result = new StringJoiner(",");
        result
                .add(callType)
                .add(servicedMsisdn)
                .add(contactedMsisdn)
                .add(Long.toString(callStartTime))
                .add(Long.toString(callEndTime));
        return result.toString();
    }
}
