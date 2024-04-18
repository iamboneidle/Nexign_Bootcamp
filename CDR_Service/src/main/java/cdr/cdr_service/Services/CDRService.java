package cdr.cdr_service.Services;

import cdr.cdr_service.CDRUtils.CDRFileSender;
import cdr.cdr_service.CDRUtils.CDRUser;
import cdr.cdr_service.CDRUtils.TransactionObject;
import cdr.cdr_service.DAO.Models.Msisdns;
import cdr.cdr_service.DAO.Models.Transactions;
import cdr.cdr_service.DAO.Repository.TransactionsRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;

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
    /**
     * Репозиторий для доступа к данным о транзакциях.
     */
    @Autowired
    private TransactionsRepository transactionsRepository;
//    TODO: почистить надо будет после того как подрубим кафку
    private static final Path ROOT_PATH = Paths.get("src/main/resources/CDRFiles").toAbsolutePath();

    /**
     * Метод, вызываемый после создания экземпляра класса.
     * Инициализирует процесс обработки CDR.
     * @throws InterruptedException Возникает, если поток прерывается во время ожидания.
     */
//    TODO: в процессе дебага сервис запускается отсюда, потом будет переделано

    @Scheduled(initialDelay = 5 * 1000)
    public void initializer() {
        List<Msisdns> msisdns = msisdnsService.getMsisdns();
        ExecutorService executor = Executors.newFixedThreadPool(msisdns.size());
        List<Future<List<TransactionObject>>> futures = new ArrayList<>();

//        for (int monthNum = 1; monthNum <= 12; monthNum++) {
        int monthNum = 1;
            CountDownLatch latch = new CountDownLatch(msisdns.size());
            int finalMonthNum = monthNum;
            try {
                msisdns.forEach(msisdn -> futures.add(executor.submit(new CDRUser(msisdn.getPhoneNumber(), finalMonthNum, latch, msisdns))));
                List<TransactionObject> transactionObjectsForMonth = new ArrayList<>();
                futures.forEach(future -> {
                    try {
                        transactionObjectsForMonth.addAll(future.get());
                    } catch (InterruptedException | ExecutionException e) {
                        System.out.println(e);
                    }
                });
                transactionObjectsForMonth.sort(Comparator.comparingLong(TransactionObject::getCallStartTime));
                latch.await();
                futures.clear();
                writeToDataBase(msisdns, transactionObjectsForMonth);
                makeCDRFiles(transactionObjectsForMonth, monthNum);
            } catch (InterruptedException e) {
                System.out.println("troubles");
            }
//        }
        executor.shutdown();
        System.out.println("DONE");
    }

    /**
     * Записывает транзакции в базу данных.
     * @param msisdns Список объектов Msisdns.
     * @param transactionObjectsForMonth Список объектов TransactionObject для месяца.
     */
    private void writeToDataBase(List<Msisdns> msisdns, List<TransactionObject> transactionObjectsForMonth) {
        List<Transactions> transactions = new ArrayList<>();
        for (TransactionObject transactionObject : transactionObjectsForMonth) {
            for (Msisdns msisdn : msisdns) {
                if (msisdn.getPhoneNumber().equals(transactionObject.getServicedMsisdnPhoneNumber())) {
                    transactions.add(new Transactions(
                            msisdn,
                            transactionObject.getCallType(),
                            transactionObject.getServicedMsisdnPhoneNumber(),
                            transactionObject.getCallStartTime(),
                            transactionObject.getCallEndTime()
                    ));
                    break;
                }
            }
        }
        transactionsRepository.saveAll(transactions);
    }

    //    TODO: этот метод нужен для подготовки файлов к отправки в Kafka, в дальнейшем он будет переделан
    private void makeCDRFiles(List<TransactionObject> transactionObjectsForMonth, int monthNum) {
        List<TransactionObject> objectsToWrite = new ArrayList<>();
        int counter = 1;

        for (int i = 0; i < transactionObjectsForMonth.size(); i++) {
            if (counter <= 10) {
                objectsToWrite.add(transactionObjectsForMonth.get(i));
                if (counter == 10) {
                    int numFile = i / counter + 1;
                    Path filePath = Paths.get(ROOT_PATH + "/" + "CDR" + monthNum + "_" + (numFile) + ".txt");
                    writer(objectsToWrite, filePath);
                    sendCdrFiles(filePath.toFile());
                    objectsToWrite.clear();
                    counter = 1;
                } else {
                    counter++;
                }
            }
        }
        if (!objectsToWrite.isEmpty()) {
            int filesQuantity = transactionObjectsForMonth.size() / 10 + 1;
            Path filePath = Paths.get(ROOT_PATH + "/" + "CDR" + monthNum + "_" + (filesQuantity) + ".txt");
            writer(objectsToWrite, filePath);
            sendCdrFiles(filePath.toFile());
        }
    }

    private void writer(List<TransactionObject> objectsToWrite, Path filePath) {
        try {
            if (!Files.exists(ROOT_PATH.toAbsolutePath())) {
                Files.createDirectory(ROOT_PATH.toAbsolutePath());
            }
            Files.deleteIfExists(filePath);
            Path file = Files.createFile(filePath);

            try (FileOutputStream outputStream = new FileOutputStream(file.toFile())) {

                for (TransactionObject obj : objectsToWrite) {
                    outputStream.write((obj.toString() + "\n").getBytes());
                    outputStream.flush();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Потом будет отправлять данные в кафку
     */
    private void sendCdrFiles(File file) {
        CDRFileSender cdrFileSender = new CDRFileSender();
        cdrFileSender.sendFile(file);
        file.delete();
    }
}
