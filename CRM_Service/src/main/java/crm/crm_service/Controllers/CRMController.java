package crm.crm_service.Controllers;

import crm.crm_service.Services.CRMService;
import crm.crm_service.CRMUtils.TariffChanger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.Authentication;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import crm.crm_service.Services.DataToChangeTariffSenderService;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class CRMController {
    @Autowired
    private CRMService crmService;
    @Autowired
    private DataToChangeTariffSenderService dataToChangeTariffSenderService;
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

    @PostMapping("/admin/change-tariff")
    public ResponseEntity<?> admin(@RequestBody String string) {
        if (!string.isEmpty()) {
            LOGGER.log(Level.INFO, string);
            Thread tariffChanger = new Thread(new TariffChanger(crmService, dataToChangeTariffSenderService));
            tariffChanger.start();
            return ResponseEntity.ok().body("CRM will change tariffs");
        }

        return ResponseEntity.badRequest().body("CRM got empty string and will not change tariffs");
    }

    @PostMapping("/admin/post-tariffs")
    public ResponseEntity<String> postTariffs(@RequestBody String string) {
        if (!string.isEmpty()) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                crmService.setMapNumberToRateId(objectMapper.readValue(string, HashMap.class));
                LOGGER.log(Level.INFO, "OK: accepted info about msisdns and their tariffs");
                return ResponseEntity.ok().body("CRM accepted tariffs info");
            } catch (JsonProcessingException e) {
                LOGGER.log(Level.SEVERE, "EXCEPTION: JsonProcessingException");
                return ResponseEntity.internalServerError().body("CRM got JsonProcessingException");
            }
        }
        LOGGER.log(Level.SEVERE, "ERROR: got empty data");
        return ResponseEntity.badRequest().body("CRM got empty tariffs info");
    }
}