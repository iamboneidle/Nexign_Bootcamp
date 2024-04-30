package hrs.hrs_service.HRSUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hrs.hrs_service.HRSUtils.RatesInfo.ClassicRateInfo;
import hrs.hrs_service.HRSUtils.RatesInfo.MonthlyRateInfo;
import hrs.hrs_service.Services.CallReceiptSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class ReceiptMaker {
    @Autowired
    private ClassicRateInfo classicRateInfo;
    @Autowired
    private MonthlyRateInfo monthlyRateInfo;
    @Autowired
    private CallReceiptSenderService callReceiptSenderService;
    private int curMonth = 1;
    private int curYear = 2024;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<String> monthlyRateUsers = new ArrayList<>();
    private static final Logger LOGGER = Logger.getLogger(ReceiptMaker.class.getName());

    public CallReceipt makeCalculation(DataToPay dataToPay) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(dataToPay.getCallTimeStart()), ZoneId.systemDefault());

        if (dateTime.getMonthValue() > curMonth) {
            curMonth++;
            sendMonthlyRateUsersReceipts();
            return rateSwitch(dataToPay);
        } else if (curMonth == 12 && dateTime.getMonthValue() == 1 && dateTime.getYear() > curYear) {
            curMonth = 1;
            curYear++;
            sendMonthlyRateUsersReceipts();
            return rateSwitch(dataToPay);
        } else {
            return rateSwitch(dataToPay);
        }
    }

    private CallReceipt rateSwitch(DataToPay dataToPay) {
        return switch (dataToPay.getRateId()) {
            case 11 -> calculateByClassicRate(dataToPay);
            case 12 -> calculateByMonthlyRate(dataToPay);
            default -> {
                LOGGER.log(Level.SEVERE, "ERROR: Unexpected value:" + dataToPay.getRateId() + "\n");
                throw new IllegalStateException("Unexpected value: " + dataToPay.getRateId() + "\n");
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
        if (dataToPay.getMinutesLeft() > 0) {
            if (!monthlyRateUsers.contains(dataToPay.getServicedMsisdnNumber())) {
                monthlyRateUsers.add(dataToPay.getServicedMsisdnNumber());
            }
            return null;
        } else {
            return calculateByClassicRate(dataToPay);
        }
    }

    private void sendMonthlyRateUsersReceipts() {
        for (String phoneNumber : monthlyRateUsers) {
            CallReceipt callReceipt = new CallReceipt(phoneNumber, null, monthlyRateInfo.getPRICE_FOR_MONTH());
            try {
                String json = objectMapper.writeValueAsString(callReceipt);
                callReceiptSenderService.sendCallReceipt(json);
            } catch (JsonProcessingException e) {
                LOGGER.log(Level.SEVERE, "EXCEPTION: " + Arrays.toString(e.getStackTrace()));
            }
        }
    }
}
