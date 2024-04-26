package brt.brt_service.Services;

import brt.brt_service.BRTUtils.CallReceipt;
import brt.brt_service.DAO.Repository.MsisdnsRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CallReceiptHandlerService {
    @Autowired
    private MsisdnsRepository msisdnsRepository;

    @Transactional
    public void validateCallReceipt(CallReceipt callReceipt) {
        updateBalance(callReceipt.getServicedMsisdnNumber(), callReceipt.getMoneyToWriteOff());
    }

    private void updateBalance(String number, float amount) {
        msisdnsRepository.writeOffMoneyByNumber(number, amount);
    }
}
