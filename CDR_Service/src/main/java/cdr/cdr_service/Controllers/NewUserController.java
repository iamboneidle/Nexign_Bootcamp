package cdr.cdr_service.Controllers;

import cdr.cdr_service.CDRUtils.DataToAddNewUserToCDR;
import cdr.cdr_service.DAO.Models.Msisdns;
import cdr.cdr_service.DAO.Repository.MsisdnsRepository;
import cdr.cdr_service.Services.CDRService;
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
    private CDRService cdrService;
    private static final Logger LOGGER = Logger.getLogger(NewUserController.class.getName());

    @PostMapping("post-new-user")
    private ResponseEntity<String> addNewUser(@RequestBody DataToAddNewUserToCDR dataToAddNewUserToCDR) {
        if (dataToAddNewUserToCDR != null) {
            Msisdns msisdn = new Msisdns(dataToAddNewUserToCDR.getMsisdn());
            msisdnsRepository.save(msisdn);
            cdrService.addNewMsisdn(msisdn);
            LOGGER.log(Level.INFO, "OK: added new user " + dataToAddNewUserToCDR.getMsisdn());
            return ResponseEntity.ok().body("CDR added user " + dataToAddNewUserToCDR.getMsisdn());
        }
        LOGGER.log(Level.SEVERE, "got empty data, can't add new user");
        return ResponseEntity.badRequest().body("CDR got empty data, can't add new user");
    }
}
