package cdr.cdr_service.Services;

import cdr.cdr_service.CDRUtils.ConcurrentQueue;
import cdr.cdr_service.CDRUtils.DataToAddNewUserToCDR;
import cdr.cdr_service.CDRUtils.DateGenerator;
import cdr.cdr_service.CDRUtils.User;
import cdr.cdr_service.DAO.Models.Msisdns;
import cdr.cdr_service.DAO.Repository.MsisdnsRepository;
import cdr.cdr_service.DAO.Repository.TransactionsRepository;
import cdr.cdr_service.Kafka.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис, отвечающий за создание потоков, выступающих в роли пользователей, создание
 * демон потока, генерирующего даты, создание конкурентной очереди и добавление новых пользователей.
 */
@Service
public class CDRService {
    /**
     * Сервис для работы с объектами Msisdns.
     */
    @Autowired
    private MsisdnsService msisdnsService;
    /**
     * Репозиторий транзакций.
     */
    @Autowired
    private TransactionsRepository transactionsRepository;
    /**
     * Кафка продюсер.
     */
    @Autowired
    private KafkaProducer kafkaProducer;
    /**
     * Репозиторий пользователей.
     */
    @Autowired
    private MsisdnsRepository msisdnsRepository;
    /**
     * Список пользователей.
     */
    private List<Msisdns> msisdns;
    /**
     * Демон поток, генерирующий даты.
     */
    private DateGenerator daemonThread;
    /**
     * Конкурентная очередь.
     */
    private ConcurrentQueue concurrentQueue;
    /**
     * Время до старта генерации звонков в секундах.
     */
    private static final int TIME_BEFORE_START = 5;

    /**
     * Метод, который вызывается через TIME_BEFORE_START секунд, получает список пользователей из базы данных,
     * создает демон поток, генерирующий новые даты, создает конкурентную очередь, для каждого пользователя создает
     * отдельный поток и запускает его.
     */
    @Scheduled(initialDelay = TIME_BEFORE_START * 1000)
    public void initializer() {
        msisdns = msisdnsService.getMsisdns();
        daemonThread = new DateGenerator();
        daemonThread.setDaemon(true);
        daemonThread.start();
        concurrentQueue = new ConcurrentQueue(msisdns, transactionsRepository, kafkaProducer);
        for (Msisdns msisdn : msisdns) {
            Thread clientThread = new Thread(new User(msisdn.getPhoneNumber(), msisdns, daemonThread, concurrentQueue));
            clientThread.start();
        }
    }

    /**
     * Метод, который добавляет нового пользователя. Он создает нового Msisdns, сохраняет его,
     * добавляет в список msisdns, чтобы о добавлении узнали все потоки, создает новый поток для
     * нового пользователя, запускает его.
     *
     * @param dataToAddNewUserToCDR Данные о новом пользователе.
     */
    public void addNewMsisdn(DataToAddNewUserToCDR dataToAddNewUserToCDR) {
        Msisdns msisdn = new Msisdns(dataToAddNewUserToCDR.getMsisdn());
        msisdnsRepository.save(msisdn);
        this.msisdns.add(msisdn);
        Thread newClientThread = new Thread(new User(msisdn.getPhoneNumber(), msisdns, daemonThread, concurrentQueue));
        newClientThread.start();
    }
}
