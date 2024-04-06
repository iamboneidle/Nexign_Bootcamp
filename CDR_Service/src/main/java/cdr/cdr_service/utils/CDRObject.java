package cdr.cdr_service.utils;

import java.util.StringJoiner;

public class CDRObject {
    private final String callType;
    private final String servicedMsisdn;
    private final String contactedMsisdn;
    private final long callStartTime;
    private final long callEndTime;

    CDRObject(String callType, String servicedMsisdn, String contactedMsisdn, long callStartTime, long callEndTime) {
        this.callType = callType;
        this.servicedMsisdn = servicedMsisdn;
        this.contactedMsisdn = contactedMsisdn;
        this.callStartTime = callStartTime;
        this.callEndTime = callEndTime;
    }

    public String getCallType() {
        return callType;
    }

    public String getServicedMsisdn() {
        return servicedMsisdn;
    }

    public String getContactedMsisdn() {
        return contactedMsisdn;
    }

    public long getCallStartTime() {
        return callStartTime;
    }

    public long getCallEndTime() { return callEndTime; }

    @Override
    public String toString() {
        StringJoiner result = new StringJoiner(",");
        result.add(callType).add(servicedMsisdn).add(contactedMsisdn).add(Long.toString(callStartTime)).add(Long.toString(callEndTime));
        return result.toString();
    }
}
