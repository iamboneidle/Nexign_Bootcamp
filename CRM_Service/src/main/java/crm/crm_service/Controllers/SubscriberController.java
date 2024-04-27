package crm.crm_service.Controllers;

import crm.crm_service.Services.CRMService;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/subscriber")
public class SubscriberController {
    @Autowired
    private CRMService crmService;

    @PostMapping("/pay")
    public ResponseEntity<?> pay() {
        crmService.pay();

        return ResponseEntity.ok("Payment successful");
    }
}
