package cdr.cdr_service.Controllers;

import cdr.cdr_service.CDRUtils.DataToAddNewUserToCDR;
import cdr.cdr_service.DAO.Models.Msisdns;
import cdr.cdr_service.DAO.Repository.MsisdnsRepository;
import cdr.cdr_service.Services.CDRService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Контроллер, на который присылается информация о добавлении нового пользователя на CRM админом.
 */
@RestController
public class NewUserController {
    /**
     * CDR сервис.
     */
    @Autowired
    private CDRService cdrService;
    /**
     * Логгер, выводящий уведомления.
     */
    private static final Logger LOGGER = Logger.getLogger(NewUserController.class.getName());

    /**
     * Сам контроллер, которым проверяет, не равное ли поступаемое RequestBody null, если нет, то
     * вызываем метод CDRService по добавлению нового пользователя логируем уведомление и возвращаем положительный респонс.
     *
     * @param dataToAddNewUserToCDR Объект, в который мапится RequestBody.
     * @return ResponseEntity с сообщением.
     */
    @NotNull
    @PostMapping("post-new-user")
    private ResponseEntity<String> addNewUser(@RequestBody DataToAddNewUserToCDR dataToAddNewUserToCDR) {
        if (dataToAddNewUserToCDR != null) {
            cdrService.addNewMsisdn(dataToAddNewUserToCDR);
            LOGGER.log(Level.INFO, "OK: added new user " + dataToAddNewUserToCDR.getMsisdn());
            return ResponseEntity.ok().body("CDR added user " + dataToAddNewUserToCDR.getMsisdn());
        }
        LOGGER.log(Level.SEVERE, "got empty data, can't add new user");
        return ResponseEntity.badRequest().body("CDR got empty data, can't add new user");
    }
}
