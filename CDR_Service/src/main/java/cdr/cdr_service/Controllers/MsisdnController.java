package cdr.cdr_service.Controllers;

import cdr.cdr_service.Services.MsisdnsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MsisdnController {
    private MsisdnsService msisdnsService;

    public MsisdnController(MsisdnsService msisdnsService) {
        this.msisdnsService = msisdnsService;
    }

    public void getUsers() {
        msisdnsService.getMsisdns();
    }
}