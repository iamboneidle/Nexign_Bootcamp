package brt.brt_service.Services.Utils;

import brt.brt_service.Postgres.DAO.Models.Msisdns;
import brt.brt_service.Postgres.DAO.Repository.MsisdnsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MsisdnsService {
    /**
     * Репозиторий абонентов.
     */
    @Autowired
    private MsisdnsRepository msisdnsRepository;

    /**
     * Метод, возвращающий всех имеющихся абонентов.
     *
     * @return Список всех абонентов.
     */
    public List<Msisdns> getMsisdns() {
        return msisdnsRepository.findAll();
    }

    /**
     * Метод для возвращения ID тарифа абонента по его номеру телефона.
     *
     * @param phoneNumber номер телефона.
     * @return ID абонента.
     */
    public long getRateIdByPhoneNumber(String phoneNumber) {
        Msisdns msisdns = msisdnsRepository.findMsisdnsByNumber(phoneNumber);
        return msisdns.getRateId();
    }
}
