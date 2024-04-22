package hrs.hrs_service.HRSUtils;

import hrs.hrs_service.HRSUtils.RatesInfo.ClassicRateInfo;
import hrs.hrs_service.HRSUtils.RatesInfo.MonthlyRateInfo;
import org.springframework.stereotype.Component;

@Component
public class ReceiptMaker {
    public CallReceipt makeCalculation(DataToPay dataToPay) {
        return switch (dataToPay.getRateId()) {
            case 11 -> calculateByClassicRate(dataToPay);
            case 12 -> calculateByMonthlyRate(dataToPay);
            default -> throw new IllegalStateException("Unexpected value: " + dataToPay.getRateId());
        };
    }

    private CallReceipt calculateByClassicRate(DataToPay dataToPay) {
        long callDuration = (dataToPay.getCallTimeEnd() - dataToPay.getCallTimeStart()) / 60 + 1;
        if (dataToPay.getCallType().equals("02")) {
            return new CallReceipt(
                    dataToPay.getServicedMsisdnNumber(),
                    null,
                    callDuration * (dataToPay.isOtherMsisdnServiced()
                            ? ClassicRateInfo.INCOMING_FROM_SERVICED
                            : ClassicRateInfo.INCOMING_FROM_OTHERS
                    )
            );
        } else {
            return new CallReceipt(
                    dataToPay.getServicedMsisdnNumber(),
                    null,
                    callDuration * (dataToPay.isOtherMsisdnServiced()
                            ? ClassicRateInfo.OUTCOMING_TO_SERVICED
                            : ClassicRateInfo.OUTCOMING_TO_OTHERS
                    )
            );
        }
    }

    private CallReceipt calculateByMonthlyRate(DataToPay dataToPay) {
        long callDuration = (dataToPay.getCallTimeEnd() - dataToPay.getCallTimeStart()) / 60 + 1;
        if (dataToPay.getMinutesLeft() >= MonthlyRateInfo.MINUTES_BY_DEFAULT) {
            return new CallReceipt(dataToPay.getServicedMsisdnNumber(), callDuration, null);
        } else {
            return calculateByClassicRate(dataToPay);
        }
    }
}
