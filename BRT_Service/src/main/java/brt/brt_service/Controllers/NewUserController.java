package brt.brt_service.Controllers;

import brt.brt_service.BRTUtils.DataToAddNewUser;
import brt.brt_service.DAO.Models.Users;
import brt.brt_service.DAO.Models.Msisdns;
import brt.brt_service.DAO.Models.Rates;
import brt.brt_service.DAO.Repository.UsersRepository;
import brt.brt_service.DAO.Repository.MsisdnsRepository;
import brt.brt_service.DAO.Repository.RatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class NewUserController {
    @Autowired
    private MsisdnsRepository msisdnsRepository;
    @Autowired
    private RatesRepository ratesRepository;
    @Autowired
    private UsersRepository usersRepository;
    private static final Logger LOGGER = Logger.getLogger(NewUserController.class.getName());

    @PostMapping("post-new-user")
    private ResponseEntity<String> addNewUser(@RequestBody DataToAddNewUser dataToAddNewUserToCDR) {
        if (dataToAddNewUserToCDR != null) {
            Users abonent = new Users(dataToAddNewUserToCDR.getName(), dataToAddNewUserToCDR.getSurname(), dataToAddNewUserToCDR.getPatronymic());
            Rates rate = ratesRepository.findRatesById(dataToAddNewUserToCDR.getTariffId());
            Msisdns msisdn = new Msisdns(dataToAddNewUserToCDR.getMsisdn(), rate, abonent, dataToAddNewUserToCDR.getMoney(), 0L, 0L);
            usersRepository.save(abonent);
            msisdnsRepository.save(msisdn);
            LOGGER.log(Level.INFO, "OK: added new user " + dataToAddNewUserToCDR.getMsisdn());
            return ResponseEntity.ok().body("BRT added user " + dataToAddNewUserToCDR.getMsisdn());
        }
        LOGGER.log(Level.SEVERE, "got empty data, can't add new user");
        return ResponseEntity.badRequest().body("BRT got empty data, can't add new user");
    }
}
