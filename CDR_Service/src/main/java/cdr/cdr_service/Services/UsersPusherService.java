package cdr.cdr_service.Services;

import cdr.cdr_service.DAO.Models.Msisdns;
import cdr.cdr_service.DAO.Repository.MsisdnsRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Этот класс нужен для того, чтобы на старте заполнить БД пользователями.
 */
@Service
public class UsersPusherService {
    /**
     * Репозиторий пользователей.
     */
    @Autowired
    private MsisdnsRepository msisdnsRepository;
    /**
     * Сервис пользователей.
     */
    @Autowired
    private MsisdnsService msisdnsService;
    /**
     * Массив с номерами пользователей.
     */
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

    /**
     * Метод, который создает пользователей на PostConstruct, если база данных пуста, по массиву
     * USERS_NUMBERS, добавляет в список phoneNumbers и сохраняет в базу данных.
     */
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
