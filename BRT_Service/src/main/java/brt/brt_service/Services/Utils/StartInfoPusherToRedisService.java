package brt.brt_service.Services.Utils;

import brt.brt_service.Postgres.DAO.Models.Msisdns;
import brt.brt_service.Postgres.DAO.Repository.MsisdnsRepository;
import brt.brt_service.Redis.DAO.Models.MsisdnToMinutesLeft;
import brt.brt_service.Redis.DAO.Repository.MsisdnToMinutesLeftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис, который отправляет данные о пользователях и остатках их минут в Redis на старте.
 */
@Service
public class StartInfoPusherToRedisService {
    /**
     * Репозиторий абонентов и остатка их минут в Redis.
     */
    @Autowired
    private MsisdnToMinutesLeftRepository msisdnToMinutesLeftRepository;
    /**
     * Репозиторий абонентов.
     */
    @Autowired
    private MsisdnsRepository msisdnsRepository;

    /**
     * Метод заносящий данные в базу данных Redis на старте.
     */
    public void pushToRedis() {
        msisdnToMinutesLeftRepository.deleteAll();
        List<MsisdnToMinutesLeft> msisdnToMinutesLeftList = new ArrayList<>();
        List<Msisdns> msisdns = msisdnsRepository.findAll();
        for (Msisdns msisdn : msisdns) {
            String phoneNumber = msisdn.getNumber();
            Long minutesLeft = msisdn.getRates().getMinLimit();
            msisdnToMinutesLeftList.add(new MsisdnToMinutesLeft(phoneNumber, minutesLeft));
        }
        msisdnToMinutesLeftRepository.saveAll(msisdnToMinutesLeftList);
    }
}
