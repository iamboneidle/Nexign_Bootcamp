package hrs.hrs_service.HRSUtils;

import hrs.hrs_service.HRSUtils.RatesInfo.ClassicRateInfo;
import hrs.hrs_service.HRSUtils.RatesInfo.MonthlyRateInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class ReceiptMaker {
    @Autowired
    ClassicRateInfo classicRateInfo;
    @Autowired
    MonthlyRateInfo monthlyRateInfo;
    private static final Logger LOGGER = Logger.getLogger(ReceiptMaker.class.getName());

    public CallReceipt makeCalculation(DataToPay dataToPay) {
        return switch (dataToPay.getRateId()) {
            case 11 -> calculateByClassicRate(dataToPay);
            case 12 -> calculateByMonthlyRate(dataToPay);
            default -> {
                LOGGER.log(Level.SEVERE, "ERROR: Unexpected value:" + dataToPay.getRateId() + "\n");
                throw new IllegalStateException("Unexpected value: " + dataToPay.getRateId());
            }
        };
    }

    private CallReceipt calculateByClassicRate(DataToPay dataToPay) {
        long callDuration = (dataToPay.getCallTimeEnd() - dataToPay.getCallTimeStart()) / 60 + 1;
        if (dataToPay.getCallType().equals("02")) {
            return new CallReceipt(
                    dataToPay.getServicedMsisdnNumber(),
                    null,
                    callDuration * (dataToPay.isOtherMsisdnServiced()
                            ? classicRateInfo.getINCOMING_FROM_SERVICED()
                            : classicRateInfo.getINCOMING_FROM_OTHERS()
                    )
            );
        } else {
            return new CallReceipt(
                    dataToPay.getServicedMsisdnNumber(),
                    null,
                    callDuration * (dataToPay.isOtherMsisdnServiced()
                            ? classicRateInfo.getOUTCOMING_TO_SERVICED()
                            : classicRateInfo.getOUTCOMING_TO_OTHERS()
                    )
            );
        }
    }

    private CallReceipt calculateByMonthlyRate(DataToPay dataToPay) {
        long callDuration = (dataToPay.getCallTimeEnd() - dataToPay.getCallTimeStart()) / 60 + 1;
        if (dataToPay.getMinutesLeft() >= monthlyRateInfo.getMINUTES_BY_DEFAULT()) {
            return new CallReceipt(dataToPay.getServicedMsisdnNumber(), callDuration, null);
        } else {
            return calculateByClassicRate(dataToPay);
        }
    }
}
