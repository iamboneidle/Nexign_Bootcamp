package brt.brt_service.Services.Utils;

import brt.brt_service.Postgres.DAO.Models.Users;
import brt.brt_service.Postgres.DAO.Models.Msisdns;
import brt.brt_service.Postgres.DAO.Models.Rates;
import brt.brt_service.Postgres.DAO.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис, который заполняет базу данных сервиса на старте, если та пуста.
 */
@Service
public class StartInfoPusherToPostgresService {
    /**
     * Репозиторий тарифов.
     */
    @Autowired
    private RatesRepository ratesRepository;
    /**
     * Репозиторий абонентов.
     */
    @Autowired
    private MsisdnsRepository msisdnsRepository;
    /**
     * Репозиторий пользователей.
     */
    @Autowired
    private UsersRepository usersRepository;
    /**
     * TODO: add description
     */
    @Autowired
    private CallsRepository callsRepository;
    /**
     * TODO: add description
     */
    @Autowired
    private CallDataRecordsRepository callDataRecordsRepository;
    /**
     * Список пользователей.
     */
    private final List<Users> users = new ArrayList<>();
    /**
     * Список тарифов.
     */
    private final List<Rates> rates = new ArrayList<>();
    /**
     * Список абнентов.
     */
    private final List<Msisdns> msisdns = new ArrayList<>();
    /**
     * Массив с номерами телефонов абонентов.
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
     * Метод, который загружает информацию в базу данных, если та пуста.
     */
    public void pushToPostgres() {
        callsRepository.deleteAll();
        msisdnsRepository.deleteAll();
        usersRepository.deleteAll();
        ratesRepository.deleteAll();
        callDataRecordsRepository.deleteAll();

        users.add(new Users("Федор", "Дмитриевич", "Панфилов"));
        users.add(new Users("Мария", "Игоревна", "Кондукова"));
        users.add(new Users("Олег", "Игоревич", "Резьбов"));
        users.add(new Users("Иосиф", "Виссарионович", "Сталин"));
        users.add(new Users("Жан-Клод", "Вандам", "Терминатор"));
        users.add(new Users("Игорь", "Евгеньевич", "Карагодин"));
        users.add(new Users("Матвей", "Сергеевич", "Соболев"));
        users.add(new Users("Галина", "Ивановна", "Виноградова"));
        users.add(new Users("Дарья", "Васильевна", "Лукашенко"));
        users.add(new Users("Дина", "Азисбековна", "Кожакова"));
        users.add(new Users("Александр", "Дмитриевич", "Мартынов"));
        usersRepository.saveAll(users);

        rates.add(new Rates(11L, "Классика", null, null, 1.5F, 2.5F, 0F, 0F));
        rates.add(new Rates(12L, "Помесячный", 100F, 50L, 1.5F, 2.5F, 0F, 0F));
        ratesRepository.saveAll(rates);

        msisdns.add(new Msisdns(USERS_NUMBERS[0], rates.get(1), users.get(0), 100F, 0L, 0L));
        msisdns.add(new Msisdns(USERS_NUMBERS[1], rates.get(1), users.get(1), 100F, 0L, 0L));
        msisdns.add(new Msisdns(USERS_NUMBERS[2], rates.get(1), users.get(2), 100F, 0L, 0L));
        msisdns.add(new Msisdns(USERS_NUMBERS[3], rates.get(1), users.get(3), 100F, 0L, 0L));
        msisdns.add(new Msisdns(USERS_NUMBERS[4], rates.get(0), users.get(4), 100F, 0L, 0L));
        msisdns.add(new Msisdns(USERS_NUMBERS[5], rates.get(0), users.get(5), 100F, 0L, 0L));
        msisdns.add(new Msisdns(USERS_NUMBERS[6], rates.get(0), users.get(6), 100F, 0L, 0L));
        msisdns.add(new Msisdns(USERS_NUMBERS[7], rates.get(0), users.get(7), 100F, 0L, 0L));
        msisdns.add(new Msisdns(USERS_NUMBERS[8], rates.get(0), users.get(8), 100F, 0L, 0L));
        msisdns.add(new Msisdns(USERS_NUMBERS[9], rates.get(0), users.get(9), 100F, 0L, 0L));
        msisdns.add(new Msisdns(USERS_NUMBERS[10], rates.get(0), users.get(10), 100F, 0L, 0L));
        msisdnsRepository.saveAll(msisdns);
    }
}
