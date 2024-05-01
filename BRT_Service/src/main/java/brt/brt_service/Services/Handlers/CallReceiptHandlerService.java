package brt.brt_service.Services.Handlers;

import brt.brt_service.BRTUtils.CallReceipt;
import brt.brt_service.DAO.Repository.MsisdnsRepository;
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
     * Метод валидирующий чеки звонкам.
     *
     * @param callReceipt чек по звонку.
     */
//    TODO: обновлять minutesLeft в кэш БД
//      добавить логику на то, присутствует ли значение минут на списание, если да, то списывать его
//      в кэш БД.
    @Transactional
    public void validateCallReceipt(CallReceipt callReceipt) {
        if (callReceipt.getMoneyToWriteOff() > 0) {
            updateBalance(callReceipt.getServicedMsisdnNumber(), callReceipt.getMoneyToWriteOff());
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
