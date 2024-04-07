package cdr.cdr_service.Services;

import cdr.cdr_service.DAO.Models.Msisdns;
import cdr.cdr_service.DAO.Repository.MsisdnsRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Этот класс нужен для того, чтобы на старте заполнить БД юзерами
 */
@Service
public class UsersPusherService {
    @Autowired
    private MsisdnsRepository msisdnsRepository;
    @Autowired
    private  MsisdnsService msisdnsService;
    private static final String[] USERS_NUMBERS = {
            "79218476904",
            "79214007407",
            "79213228791",
            "79217404007",
            "79215468901",
            "79211428315",
            "79211687111",
            "79218320820",
            "79211422108",
            "79210138023",
            "79212418053",
    };

    @PostConstruct
    private void pushToDB() {
        if (msisdnsService.getMsisdns().isEmpty()) {
            List<Msisdns> phoneNumbers = new ArrayList<>();
            for (String num : USERS_NUMBERS) {
                phoneNumbers.add(new Msisdns(num));
            }
            msisdnsRepository.saveAll(phoneNumbers);
        }
    }
}

//INSERT INTO msisdns (phone_number) VALUES ('79218476904');
//INSERT INTO msisdns (phone_number) VALUES ('79214007407');
//INSERT INTO msisdns (phone_number) VALUES ('79213228791');
//INSERT INTO msisdns (phone_number) VALUES ('79217404007');
//INSERT INTO msisdns (phone_number) VALUES ('79215468901');
//INSERT INTO msisdns (phone_number) VALUES ('79211428315');
//INSERT INTO msisdns (phone_number) VALUES ('79211687111');
//INSERT INTO msisdns (phone_number) VALUES ('79218320820');
//INSERT INTO msisdns (phone_number) VALUES ('79211422108');
//INSERT INTO msisdns (phone_number) VALUES ('79210138023');
//INSERT INTO msisdns (phone_number) VALUES ('79212418053');