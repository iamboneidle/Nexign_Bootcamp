package crm.crm_service.Controllers;

import crm.crm_service.Services.CRMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manager")
public class ManagerController {
    @Autowired
    private CRMService crmService;

    @PostMapping("/change-tariff")
    public ResponseEntity<?> changeTariff() {
        crmService.changeTariff();

        return ResponseEntity.ok("Tariff changed");
    }

    @PostMapping("/save")
    public ResponseEntity<?> save() {
        crmService.save();

        return ResponseEntity.ok("Saved");
    }

    @PostMapping("/add-subscriber")
    public ResponseEntity<?> addSubscriber() {
        crmService.addSubscriber();

        return ResponseEntity.ok("subscriber was added");
    }
}
