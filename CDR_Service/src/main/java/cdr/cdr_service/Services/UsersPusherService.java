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
 * Пока что в описание его лучше не включать. Запуск сервиса пока что не сделан тем образом, которым он должен быть
 * можно сказать, что он еще в дебаге.
 */
@Service
public class UsersPusherService {
    @Autowired
    private MsisdnsRepository msisdnsRepository;
    @Autowired
    private MsisdnsService msisdnsService;
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
