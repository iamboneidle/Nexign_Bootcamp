package crm.crm_service.Services;

import crm.crm_service.DAO.Models.Users;
import crm.crm_service.DAO.Repository.UsersRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис, который отвечает за заполнение БД на старте информацией о пользователях.
 */
@Service
public class StartInfoPusherService {
    /**
     * Репозиторий пользователей.
     */
    @Autowired
    private UsersRepository usersRepository;
    /**
     * Имена пользователей в системе.
     */
    private static final String[] USERNAMES = {
            "admin",
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
     * Пароли пользователей в системе.
     */
    private static final String[] PASSWORDS = {
            "$2a$10$9OwUZRwfZ/zrggwJ6ZDXreWbbCX1Q8OYHh15lPvGDPUBga.ifJ66G",
            "$2a$10$/KNnNb5iScV/sWinUSV5m.HRh.snnqfNJ3RYBwuEtuM9Pzo2fVhUG",
            "$2a$10$/KNnNb5iScV/sWinUSV5m.HRh.snnqfNJ3RYBwuEtuM9Pzo2fVhUG",
            "$2a$10$/KNnNb5iScV/sWinUSV5m.HRh.snnqfNJ3RYBwuEtuM9Pzo2fVhUG",
            "$2a$10$/KNnNb5iScV/sWinUSV5m.HRh.snnqfNJ3RYBwuEtuM9Pzo2fVhUG",
            "$2a$10$/KNnNb5iScV/sWinUSV5m.HRh.snnqfNJ3RYBwuEtuM9Pzo2fVhUG",
            "$2a$10$/KNnNb5iScV/sWinUSV5m.HRh.snnqfNJ3RYBwuEtuM9Pzo2fVhUG",
            "$2a$10$/KNnNb5iScV/sWinUSV5m.HRh.snnqfNJ3RYBwuEtuM9Pzo2fVhUG",
            "$2a$10$/KNnNb5iScV/sWinUSV5m.HRh.snnqfNJ3RYBwuEtuM9Pzo2fVhUG",
            "$2a$10$/KNnNb5iScV/sWinUSV5m.HRh.snnqfNJ3RYBwuEtuM9Pzo2fVhUG",
            "$2a$10$/KNnNb5iScV/sWinUSV5m.HRh.snnqfNJ3RYBwuEtuM9Pzo2fVhUG",
            "$2a$10$/KNnNb5iScV/sWinUSV5m.HRh.snnqfNJ3RYBwuEtuM9Pzo2fVhUG",
    };
    /**
     * Роли пользователей в системе.
     */
    private static final String[] ROLES = {
            "MANAGER",
            "SUBSCRIBER",
            "SUBSCRIBER",
            "SUBSCRIBER",
            "SUBSCRIBER",
            "SUBSCRIBER",
            "SUBSCRIBER",
            "SUBSCRIBER",
            "SUBSCRIBER",
            "SUBSCRIBER",
            "SUBSCRIBER",
            "SUBSCRIBER",
    };

    /**
     * Метод, отправляющий данные в БД на старте.
     */
    @PostConstruct
    public void pushToDB() {
        usersRepository.deleteAll();
        List<Users> usersToPush = new ArrayList<>();
        for (int i = 0; i < USERNAMES.length; i++) {
            usersToPush.add(new Users(USERNAMES[i], PASSWORDS[i], ROLES[i]));
        }
        usersRepository.saveAll(usersToPush);
    }
}
