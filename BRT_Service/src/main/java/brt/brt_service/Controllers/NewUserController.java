package brt.brt_service.Controllers;

import brt.brt_service.BRTUtils.DataToAddNewUser;
import brt.brt_service.Postgres.DAO.Models.Users;
import brt.brt_service.Postgres.DAO.Models.Msisdns;
import brt.brt_service.Postgres.DAO.Models.Rates;
import brt.brt_service.Postgres.DAO.Repository.UsersRepository;
import brt.brt_service.Postgres.DAO.Repository.MsisdnsRepository;
import brt.brt_service.Postgres.DAO.Repository.RatesRepository;
import brt.brt_service.Redis.DAO.Models.MsisdnToMinutesLeft;
import brt.brt_service.Redis.DAO.Repository.MsisdnToMinutesLeftRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс контроллера, принимающий данные о добавлении нового пользователя.
 */
@RestController
public class NewUserController {
    /**
     * Репозиторий абонентов.
     */
    @Autowired
    private MsisdnsRepository msisdnsRepository;
    /**
     * Репозиторий тарифов.
     */
    @Autowired
    private RatesRepository ratesRepository;
    /**
     * Репозиторий пользователей.
     */
    @Autowired
    private UsersRepository usersRepository;
    /**
     * Репозиторий абонентов и остатка их минут в Redis.
     */
    @Autowired
    private MsisdnToMinutesLeftRepository msisdnToMinutesLeftRepository;
    /**
     * Логгер, выводящий уведомления.
     */
    private static final Logger LOGGER = Logger.getLogger(NewUserController.class.getName());

    /**
     * Контроллер, принимающий информацию о добавлении нового тарифа.
     *
     * @param dataToAddNewUser Объект, в который мапится RequestBody.
     * @return ResponseEntity со статусом ответа.
     */
    @PostMapping("/post-new-user")
    private ResponseEntity<String> addNewUser(@RequestBody DataToAddNewUser dataToAddNewUser) {
        if (ObjectUtils.allNotNull(dataToAddNewUser.getMoney(), dataToAddNewUser.getTariffId(),
                dataToAddNewUser.getSurname(), dataToAddNewUser.getName(),
                dataToAddNewUser.getPatronymic(), dataToAddNewUser.getMsisdn())) {
            Users abonent = new Users(dataToAddNewUser.getName(), dataToAddNewUser.getSurname(), dataToAddNewUser.getPatronymic());
            Rates rate = ratesRepository.findRatesById(dataToAddNewUser.getTariffId());
            Msisdns msisdn = new Msisdns(dataToAddNewUser.getMsisdn(), rate, abonent, dataToAddNewUser.getMoney(), 0L, 0L);
            usersRepository.save(abonent);
            msisdnsRepository.save(msisdn);
            msisdnToMinutesLeftRepository.save(new MsisdnToMinutesLeft(dataToAddNewUser.getMsisdn(), rate.getMinLimit()));
            LOGGER.log(Level.INFO, "OK: added new user " + dataToAddNewUser.getMsisdn());
            return ResponseEntity.ok().body("BRT added user " + dataToAddNewUser.getMsisdn());
        }
        LOGGER.log(Level.SEVERE, "got empty data, can't add new user");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("BRT got empty data, can't add new user");
    }
}
