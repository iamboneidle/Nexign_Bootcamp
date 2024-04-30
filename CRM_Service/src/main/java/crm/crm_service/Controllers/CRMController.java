package crm.crm_service.Controllers;

import crm.crm_service.CRMExecutors.MoneyPutter;
import crm.crm_service.CRMExecutors.TariffChanger;
import crm.crm_service.CRMUtils.*;
import crm.crm_service.Services.CRMService;
import crm.crm_service.Services.DataToPutMoneySenderService;
import crm.crm_service.Services.NewUserAdderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import crm.crm_service.Services.DataToChangeTariffSenderService;

import java.util.Base64;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class CRMController {
    @Autowired
    private CRMService crmService;
    @Autowired
    private DataToChangeTariffSenderService dataToChangeTariffSenderService;
    @Autowired
    private DataToPutMoneySenderService dataToPutMoneySenderService;
    @Autowired
    private NewUserAdderService newUserAdderService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String USER_PASSWORD = "user";
    private static final Logger LOGGER = Logger.getLogger(CRMController.class.getName());

    @GetMapping("/")
    public String home() {
        return "<h1>Welcome home!</h1>";
    }

    @GetMapping("/user")
    public String user(Authentication authentication) {
        return "<h1>Welcome User!</h1><h2>" + authentication.getName() + "</h2>";
    }

    @GetMapping("/admin")
    public String admin(Authentication authentication) {
        return "<h1>Welcome Admin!</h1><h2>" + authentication.getName() + " " + authentication.getAuthorities() + "</h2>";
    }

    @PostMapping("/admin/change-tariff-monthly")
    public ResponseEntity<String> changeTariffMonthly(@RequestBody String string) {
        if (!string.isEmpty()) {
            LOGGER.log(Level.INFO, string + " we will change some tariffs");
            Thread tariffChanger = new Thread(new TariffChanger(crmService, dataToChangeTariffSenderService));
            tariffChanger.start();
            return ResponseEntity.ok().body("CRM will change tariffs");
        }
        LOGGER.log(Level.SEVERE, "ERROR: got empty string, won't change tariffs");
        return ResponseEntity.badRequest().body("CRM got empty string and will not change tariffs");
    }

    @PostMapping("/admin/change-tariff")
    public ResponseEntity<String> changeTariff(@RequestBody DataToChangeTariff dataToChangeTariff) {
        if (dataToChangeTariff != null) {
            try {
                dataToChangeTariffSenderService.sendDataToChangeTariff(objectMapper.writeValueAsString(dataToChangeTariff));
                LOGGER.log(Level.INFO, "OK: Tariff for " + dataToChangeTariff.getMsisdn() +
                        " was changed to " + dataToChangeTariff.getTariffId());
                return ResponseEntity.ok("CRM: You changed tariff for " + dataToChangeTariff.getMsisdn() +
                        " to " + dataToChangeTariff.getTariffId());
            } catch (JsonProcessingException e) {
                LOGGER.log(Level.SEVERE, "EXCEPTION: JsonProcessingException");
                return ResponseEntity.internalServerError().body("CRM: JsonProcessingException");
            }
        }
        LOGGER.log(Level.SEVERE, "ERROR: got empty data, can't change tariff");
        return ResponseEntity.badRequest().body("CRM: empty data, can't change tariff");
    }

    @PostMapping("/admin/save")
    public ResponseEntity<String> save(@RequestBody DataToAddNewUser dataToAddNewUser) {
        if (dataToAddNewUser != null) {
            if (!crmService.getMapNumberToRateId().containsKey(dataToAddNewUser.getMsisdn())) {
                newUserAdderService.add(dataToAddNewUser);
                LOGGER.log(Level.INFO, "OK: \nnew user: \n" +
                        "name: " + dataToAddNewUser.getName() + "\n" +
                        "surname: " + dataToAddNewUser.getSurname() + "\n" +
                        "patronymic: " + dataToAddNewUser.getPatronymic() + "\n" +
                        "msisdn: " + dataToAddNewUser.getMsisdn() + "\n" +
                        "tariffId: " + dataToAddNewUser.getTariffId() + "\n" +
                        "money: " + dataToAddNewUser.getMoney() + "\n"
                );
                return ResponseEntity.ok().body("CRM added user " + dataToAddNewUser.getMsisdn() + " successfully");
            }
            LOGGER.log(Level.SEVERE, "ERROR: User with this msisdn: " + dataToAddNewUser.getMsisdn() +
                    " already exists, can't add");
            return ResponseEntity.badRequest().body("CRM: User with this msisdn: " + dataToAddNewUser.getMsisdn() +
                    " already exists");
        }
        LOGGER.log(Level.SEVERE, "ERROR: got empty data, can't add new user");
        return ResponseEntity.badRequest().body("CRM: empty data, can't save new user");
    }

    @PostMapping("/admin/put-money-monthly")
    public ResponseEntity<?> putMoneyMonthly(@RequestBody String string) {
        if (!string.isEmpty()) {
            LOGGER.log(Level.INFO, string + " we will put some money");
            Thread moneyPutter = new Thread(new MoneyPutter(crmService, dataToPutMoneySenderService));
            moneyPutter.start();
            return ResponseEntity.ok().body("CRM will put money");
        }
        LOGGER.log(Level.SEVERE, "ERROR: got empty string, won't put money");
        return ResponseEntity.badRequest().body("CRM got empty string and will not put money");
    }

    @PostMapping("/admin/post-tariffs")
    public ResponseEntity<String> postTariffs(@RequestBody String string) {
        if (!string.isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                crmService.setMapNumberToRateId(objectMapper.readValue(string, HashMap.class));
                if (!crmService.getMapNumberToRateId().isEmpty()) {
                    LOGGER.log(Level.INFO, "OK: accepted info about msisdn and their and it is not empty");
                    return ResponseEntity.ok().body("CRM accepted info about msisdns and their tariffs");
                }
                LOGGER.log(Level.SEVERE, "ERROR: accepted empty data and gonna crash in a month");
                return ResponseEntity.badRequest().body("CRM accepted empty data about tariffs");
            } catch (JsonProcessingException e) {
                LOGGER.log(Level.SEVERE, "EXCEPTION: JsonProcessingException");
                return ResponseEntity.internalServerError().body("CRM got JsonProcessingException");
            }
        }
        LOGGER.log(Level.SEVERE, "ERROR: got empty data");
        return ResponseEntity.badRequest().body("CRM got empty tariffs info");
    }

    @PostMapping("/user/put-money")
    public ResponseEntity<String> putMoney(@RequestBody DataToPutMoney dataToPutMoney, @RequestHeader("Authorization") String authorization) {
        if (dataToPutMoney != null) {
            if (compareUsernames(authorization, dataToPutMoney.getMsisdn())) {
                try {
                    dataToPutMoneySenderService.sendDataToPutMoney(objectMapper.writeValueAsString(dataToPutMoney));
                } catch (JsonProcessingException e) {
                    LOGGER.log(Level.SEVERE, "EXCEPTION: JsonProcessingException");
                    return ResponseEntity.internalServerError().body("CRM: JsonProcessingException");
                }
                LOGGER.log(Level.INFO, "User " + dataToPutMoney.getMsisdn() +
                        " put " + dataToPutMoney.getMoney() + " on his account");
                return ResponseEntity.ok().body("CRM: You put " + dataToPutMoney.getMoney() + " on your account");
            }
            LOGGER.log(Level.SEVERE, "ERROR: User put not his number");
            return ResponseEntity.badRequest().body("CRM: NOT YOUR NUMBER");
        }
        LOGGER.log(Level.SEVERE, "ERROR: User put empty data");
        return ResponseEntity.badRequest().body("CRM: data is empty");
    }

    private boolean compareUsernames(String header, String username) {
        String valueToEncode = username + ":" + USER_PASSWORD;
        return ("Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes())).equals(header);
    }
}