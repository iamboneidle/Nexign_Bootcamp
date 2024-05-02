package brt.brt_service.Services.Handlers;

import brt.brt_service.BRTUtils.CallReceipt;
import brt.brt_service.Postgres.DAO.Repository.MsisdnsRepository;
import brt.brt_service.Redis.DAO.Models.MsisdnToMinutesLeft;
import brt.brt_service.Redis.DAO.Repository.MsisdnToMinutesLeftRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Класс, обрабатывающий чек по звонку.
 */
@Service
public class CallReceiptHandlerService {
    /**
     * Репозиторий абонентов.
     */
    @Autowired
    private MsisdnsRepository msisdnsRepository;
    /**
     * Репозиторий абонентов и остатка их минут в Redis.
     */
    @Autowired
    private MsisdnToMinutesLeftRepository msisdnToMinutesLeftRepository;

    /**
     * Метод валидирующий чеки звонкам.
     *
     * @param callReceipt чек по звонку.
     */
    @Transactional
    public void validateCallReceipt(CallReceipt callReceipt) {
        if (callReceipt.getMoneyToWriteOff() > 0) {
            updateBalance(callReceipt.getServicedMsisdnNumber(), callReceipt.getMoneyToWriteOff());
        }
        if (callReceipt.getMinutesToWriteOff() != null) {
            MsisdnToMinutesLeft msisdnToMinutesLeft = msisdnToMinutesLeftRepository.findById(callReceipt.getServicedMsisdnNumber()).orElse(null);
            if (msisdnToMinutesLeft != null) {
                long newMinutes = msisdnToMinutesLeft.getMinutesLeft() - callReceipt.getMinutesToWriteOff();
                msisdnToMinutesLeft.setMinutesLeft(Math.max(newMinutes, 0L)
                );
                msisdnToMinutesLeftRepository.save(msisdnToMinutesLeft);
            }
        }
    }

    /**
     * Метод, обновляющий баланс на счете абонента по чеку за звонок.
     *
     * @param number Номер телефона абонента.
     * @param amount Количество денег на списание.
     */
    private void updateBalance(String number, float amount) {
        msisdnsRepository.writeOffMoneyByNumber(number, amount);
    }
}
