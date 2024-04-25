package cdr.cdr_service.Services;

import cdr.cdr_service.CDRUtils.ConcurrentQueue;
import cdr.cdr_service.CDRUtils.DateGenerator;
import cdr.cdr_service.CDRUtils.User;
import cdr.cdr_service.DAO.Models.Msisdns;
import cdr.cdr_service.DAO.Repository.TransactionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для формирования CDR-файлов, записи в базу данных (пока что, мб потом декомпозирую)
 * и для отправки в кафку (опять же пока что)
 */
@Service
public class CDRService {
    /**
     * Сервис для работы с объектами Msisdns.
     */
    @Autowired
    private MsisdnsService msisdnsService;
    @Autowired
    private CDRFileSenderService cdrFileSenderService;
    @Autowired
    private TransactionsRepository transactionsRepository;

    /**
     * Метод, вызываемый после создания экземпляра класса.
     * Инициализирует процесс обработки CDR.
     */
    @Scheduled(initialDelay = 5 * 1000)
    public void initializer() {
        List<Msisdns> msisdns = msisdnsService.getMsisdns();
        DateGenerator daemonThread = new DateGenerator();
        daemonThread.setDaemon(true);
        daemonThread.start();
        ConcurrentQueue concurrentQueue = new ConcurrentQueue(msisdns, cdrFileSenderService, transactionsRepository);
        for (Msisdns msisdn : msisdns) {
            Thread clientThread = new Thread(new User(msisdn.getPhoneNumber(), msisdns, daemonThread, concurrentQueue));
            clientThread.start();
        }
    }
}
