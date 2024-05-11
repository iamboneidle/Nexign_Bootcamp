package crm.crm_service.Controllers;

import crm.crm_service.CRMExecutors.MoneyPutter;
import crm.crm_service.CRMExecutors.TariffChanger;
import crm.crm_service.CRMUtils.*;
import crm.crm_service.Services.CRMService;
import crm.crm_service.Services.DataToPutMoneySenderService;
import crm.crm_service.Services.NewUserAdderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import crm.crm_service.Services.DataToChangeTariffSenderService;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс контроллеров CRM сервиса.
 */
@RestController
public class CRMController {
    /**
     * CRMService.
     */
    @Autowired
    private CRMService crmService;
    /**
     * Сервис по отправке данных на смену тарифа в BRT.
     */
    @Autowired
    private DataToChangeTariffSenderService dataToChangeTariffSenderService;
    /**
     * Сервис по отправке данных на пополнение счета в BRT.
     */
    @Autowired
    private DataToPutMoneySenderService dataToPutMoneySenderService;
    /**
     * Сревис по добавлению нового пользователя.
     */
    @Autowired
    private NewUserAdderService newUserAdderService;
    /**
     * Объект ObjectMapper для создания Json из объекта.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();
    /**
     * Пароль для всех пользователей.
     */
    private static final String USER_PASSWORD = "user";
    /**
     * Логгер для выводы уведомлений.
     */
    private static final Logger LOGGER = Logger.getLogger(CRMController.class.getName());

    /**
     * Контроллер домашней страницы, доступной всем.
     *
     * @return Приветствие (html).
     */
    @GetMapping("/")
    @Tag(name = "Everyone")
    @Operation(summary = "This method is used to see welcome page.")
    public String home() {
        return "<h1>Welcome home!</h1>";
    }

    /**
     * Контроллер домашней страницы, доступной пользователям.
     *
     * @param authentication Объект аутентификации, нужен для приветствия.
     * @return Приветствие (html).
     */
    @GetMapping("/user")
    @Tag(name = "Subscriber")
    @Operation(summary = "This method is used to see user page.")
    public String user(Authentication authentication) {
        return "<h1>Welcome User!</h1><h2>" + authentication.getName() + "</h2>";
    }

    /**
     * Контроллер домашней страницы, доступной админу.
     *
     * @param authentication Объект аутентификации, нужен для приветствия.
     * @return Приветствие (html).
     */
    @GetMapping("/admin")
    @Tag(name = "Manager")
    @Operation(summary = "This method is used to see admin page.")
    public String admin(Authentication authentication) {
        return "<h1>Welcome Admin!</h1><h2>" + authentication.getName() + " " + authentication.getAuthorities() + "</h2>";
    }

    /**
     * Контроллер, на который поступает запрос из BRT сервиса о том, что настал новый месяц и нужно
     * поменять тарифы от 1 до 3 случайным пользователям. После запроса на контроллер
     * создается новый поток, который генерирует нужную информацию и отправляет ее в BRT.
     *
     * @param string Строка с новым месяцем.
     * @return ResponseEntity с информацией об успешности запроса.
     */
    @PostMapping("/admin/change-tariff-monthly")
    @Tag(name = "Manager")
    @Operation(summary = "This method is used to change tariffs every month, BRT uses it.")
    public ResponseEntity<String> changeTariffMonthly(@RequestBody String string, @RequestHeader("Authorization") String authorization) {
        if (!string.isEmpty()) {
            LOGGER.log(Level.INFO, string + " we will change some tariffs");
            Thread tariffChanger = new Thread(new TariffChanger(crmService, dataToChangeTariffSenderService));
            tariffChanger.start();
            return ResponseEntity.ok().body("CRM will change tariffs");
        }
        LOGGER.log(Level.SEVERE, "ERROR: got empty string, won't change tariffs");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CRM got empty string and will not change tariffs");
    }

    /**
     * Контроллер, на который поступает запрос из BRT на пополнение баланса каждого абонента на случайную
     * величину. После того как пришел запрос, создается новый поток,
     * который генерирует нужную информацию и посылает ее в BRT.
     *
     * @param string Строка с новым месяцем.
     * @return ResponseEntity с информацией об успешности запроса.
     */
    @PostMapping("/admin/put-money-monthly")
    @Tag(name = "Manager")
    @Operation(summary = "This method is used to put money every month, BRT uses it.")
    public ResponseEntity<?> putMoneyMonthly(@RequestBody String string, @RequestHeader("Authorization") String authorization) {
        if (!string.isEmpty()) {
            LOGGER.log(Level.INFO, string + " we will put some money");
            Thread moneyPutter = new Thread(new MoneyPutter(crmService, dataToPutMoneySenderService));
            moneyPutter.start();
            return ResponseEntity.ok().body("CRM will put money");
        }
        LOGGER.log(Level.SEVERE, "ERROR: got empty string, won't put money");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CRM got empty string and will not put money");
    }

    /**
     * Контроллер, который использует админ для смены тарифа конкретному пользователю.
     *
     * @param dataToChangeTariff Объект в который мапится RequestBody.
     * @return ResponseEntity с информацией об успешности запроса.
     */
    @PostMapping("/admin/change-tariff")
    @Tag(name = "Manager")
    @Operation(summary = "This method is used by admin to change certain msisdn's tariff.")
    public ResponseEntity<String> changeTariff(@RequestBody DataToChangeTariff dataToChangeTariff, @RequestHeader("Authorization") String authorization) {
        if (ObjectUtils.allNotNull(dataToChangeTariff.getTariffId(), dataToChangeTariff.getMsisdn())) {
            Map<String, Long> mapNumberToRateId = crmService.getMapNumberToRateId();
            if (mapNumberToRateId.containsKey(dataToChangeTariff.getMsisdn())) {
                if (!Objects.equals(dataToChangeTariff.getTariffId(), mapNumberToRateId.get(dataToChangeTariff.getMsisdn()))) {
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
                LOGGER.log(Level.SEVERE, "ERROR: user " + dataToChangeTariff.getMsisdn() +
                        " already has " + dataToChangeTariff.getTariffId() + " tariff");
                return ResponseEntity.status(HttpStatus.CONFLICT).body("CRM: user " + dataToChangeTariff.getMsisdn() +
                        " already has " + dataToChangeTariff.getTariffId() + " tariff");
            }
            LOGGER.log(Level.SEVERE, "ERROR: user with this number: " + dataToChangeTariff.getMsisdn() +
                    " doesn't exist");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("CRM: user with this number: " + dataToChangeTariff.getMsisdn() +
                    " doesn't exist");
        }
        LOGGER.log(Level.SEVERE, "ERROR: got empty data, can't change tariff");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CRM: empty data, can't change tariff");
    }

    /**
     * Контроллер, который использует админ для добавления нового пользователя.
     *
     * @param dataToAddNewUser Объект в который мапится RequestBody.
     * @return ResponseEntity с информацией об успешности запроса.
     */
    @PostMapping("/admin/save")
    @Tag(name = "Manager")
    @Operation(summary = "This method is used by admin to add new user.")
    public ResponseEntity<String> save(@RequestBody DataToAddNewUser dataToAddNewUser, @RequestHeader("Authorization") String authorization) {
        if (ObjectUtils.allNotNull(dataToAddNewUser.getMoney(), dataToAddNewUser.getTariffId(),
                dataToAddNewUser.getSurname(), dataToAddNewUser.getName(),
                dataToAddNewUser.getPatronymic(), dataToAddNewUser.getMsisdn())) {
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
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CRM: empty data, can't save new user");
    }

    /**
     * Контроллер, на который "проливается" информация о пользователях и их тарифах при запуске BRT
     * сервиса, чтобы CRM знал информацию, по которой будут формироваться данные о
     * ежемесячной смене тарифов пользователями и пополнении их балансов.
     *
     * @param string Строка с ХэшМап внутри с информацией о пользователе и его тарифе.
     * @return ResponseEntity с информацией об успешности запроса.
     */
    @PostMapping("/admin/post-tariffs")
    @Tag(name = "Manager")
    @Operation(summary = "This method is used to acknowledge CRM about msisdns and their tariff, BRT uses it.")
    public ResponseEntity<String> postTariffs(@RequestBody String string, @RequestHeader("Authorization") String authorization) {
        if (!string.isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                crmService.setMapNumberToRateId(objectMapper.readValue(string, HashMap.class));
                if (!crmService.getMapNumberToRateId().isEmpty()) {
                    LOGGER.log(Level.INFO, "OK: accepted info about msisdns and their tariffs, and it is not empty");
                    return ResponseEntity.ok().body("CRM accepted info about msisdns and their tariffs");
                }
                LOGGER.log(Level.SEVERE, "ERROR: accepted empty data and gonna crash in a month");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("CRM accepted empty data about tariffs");
            } catch (JsonProcessingException e) {
                LOGGER.log(Level.SEVERE, "EXCEPTION: JsonProcessingException");
                return ResponseEntity.internalServerError().body("CRM got JsonProcessingException");
            }
        }
        LOGGER.log(Level.SEVERE, "ERROR: got empty data");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CRM got empty tariffs info");
    }

    /**
     * Контроллер, который использует пользователь для того, чтобы положить себе деньги на счет.
     * Объект авторизации нужен для того, чтобы проверить, кладет пользователь деньги себе на счет или нет.
     *
     * @param dataToPutMoney Объект в который мапится RequestBody.
     * @param authorization Объект с данными авторизации пользователей.
     * @return ResponseEntity с информацией об успешности запроса.
     */
    @PostMapping("/user/put-money")
    @Tag(name = "Subscriber")
    @Operation(summary = "This method is by msisdn to put money on account.")
    public ResponseEntity<String> putMoney(@RequestBody DataToPutMoney dataToPutMoney, @RequestHeader("Authorization") String authorization) {
        if (ObjectUtils.allNotNull(dataToPutMoney.getMoney(), dataToPutMoney.getMsisdn())) {
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
            LOGGER.log(Level.SEVERE, "ERROR: User puts not on his number");
            return ResponseEntity.status(HttpStatus.CONFLICT).body("CRM: NOT YOUR NUMBER");
        }
        LOGGER.log(Level.SEVERE, "ERROR: User put empty data");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("CRM: data is empty");
    }

    /**
     * Метод, который сверяет данные авторизации.
     *
     * @param header То, как данные авторизации выглядят.
     * @param username Имя пользователя, чтобы понять, как данные авторизации должны выглядеть на самом деле.
     * @return true или false значение в зависимости от успешности проверки.
     */
    private boolean compareUsernames(String header, String username) {
        String valueToEncode = username + ":" + USER_PASSWORD;
        return ("Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes())).equals(header);
    }
}