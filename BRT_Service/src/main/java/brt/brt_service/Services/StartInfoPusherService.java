package brt.brt_service.Services;

import brt.brt_service.DAO.Models.Abonents;
import brt.brt_service.DAO.Models.Msisdns;
import brt.brt_service.DAO.Models.Rates;
import brt.brt_service.DAO.Repository.AbonentsRepository;
import brt.brt_service.DAO.Repository.MsisdnsRepository;
import brt.brt_service.DAO.Repository.RatesRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StartInfoPusherService {
    @Autowired
    private RatesRepository ratesRepository;
    @Autowired
    private MsisdnsRepository msisdnsRepository;
    @Autowired
    private AbonentsRepository abonentsRepository;
    private final List<Abonents> abonents = new ArrayList<>();;
    private final List<Rates> rates = new ArrayList<>();
    private final List<Msisdns> msisdns = new ArrayList<>();
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
        if (abonentsRepository.findAll().isEmpty()) {
            abonents.add(new Abonents("Федор", "Дмитриевич", "Панфилов"));
            abonents.add(new Abonents("Мария", "Игоревна", "Кондукова"));
            abonents.add(new Abonents("Олег", "Игоревич", "Резьбов"));
            abonents.add(new Abonents("Иосиф", "Виссарионович", "Сталин"));
            abonents.add(new Abonents("Жан-Клод", "Вандам", "Терминатор"));
            abonents.add(new Abonents("Игорь", "Евгеньевич", "Карагодин"));
            abonents.add(new Abonents("Матвей", "Сергеевич", "Соболев"));
            abonents.add(new Abonents("Галина", "Ивановна", "Виноградова"));
            abonents.add(new Abonents("Дарья", "Васильевна", "Лукошенко"));
            abonents.add(new Abonents("Дина", "Азисбековна", "Кожакова"));
            abonents.add(new Abonents("Александр", "Дмитриевич", "Мартынов"));
            abonentsRepository.saveAll(abonents);
        }
        if (ratesRepository.findAll().isEmpty()) {
            rates.add(new Rates(11L, "Классика", null, null, 1.5F, 2.5F, 0F, 0F));
            rates.add(new Rates(12L, "Помесячный", 100F, 50F, 1.5F, 2.5F, 0F, 0F));
            ratesRepository.saveAll(rates);
        }
        if (msisdnsRepository.findAll().isEmpty()) {
            msisdns.add(new Msisdns(USERS_NUMBERS[0], rates.get(1), abonents.get(0), 100F, 0L, 0L));
            msisdns.add(new Msisdns(USERS_NUMBERS[1], rates.get(1), abonents.get(1), 100F, 0L, 0L));
            msisdns.add(new Msisdns(USERS_NUMBERS[2], rates.get(1), abonents.get(2), 100F, 0L, 0L));
            msisdns.add(new Msisdns(USERS_NUMBERS[3], rates.get(1), abonents.get(3), 100F, 0L, 0L));
            msisdns.add(new Msisdns(USERS_NUMBERS[4], rates.get(0), abonents.get(4), 100F, 0L, 0L));
            msisdns.add(new Msisdns(USERS_NUMBERS[5], rates.get(0), abonents.get(5), 100F, 0L, 0L));
            msisdns.add(new Msisdns(USERS_NUMBERS[6], rates.get(0), abonents.get(6), 100F, 0L, 0L));
            msisdns.add(new Msisdns(USERS_NUMBERS[7], rates.get(0), abonents.get(7), 100F, 0L, 0L));
            msisdns.add(new Msisdns(USERS_NUMBERS[8], rates.get(0), abonents.get(8), 100F, 0L, 0L));
            msisdns.add(new Msisdns(USERS_NUMBERS[9], rates.get(0), abonents.get(9), 100F, 0L, 0L));
            msisdns.add(new Msisdns(USERS_NUMBERS[10], rates.get(0), abonents.get(10), 100F, 0L, 0L));
            msisdnsRepository.saveAll(msisdns);
        }
    }
}
