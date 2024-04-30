package cdr.cdr_service.CDRUtils;

import cdr.cdr_service.DAO.Models.Msisdns;
import cdr.cdr_service.DAO.Models.Transactions;
import cdr.cdr_service.DAO.Repository.TransactionsRepository;
import cdr.cdr_service.Services.CDRFileSenderService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ConcurrentQueue {
    private List<TransactionObject> queue = new ArrayList<>();
    private long cdrFileCounter = 0;
    private final CDRFileSenderService cdrFileSenderService;
    private final TransactionsRepository transactionsRepository;
    private final List<Msisdns> msisdns;
    private final Path ROOT_PATH = Paths.get("src/main/resources/CDRFiles").toAbsolutePath();
    private static final Logger LOGGER = Logger.getLogger(ConcurrentQueue.class.getName());
    private static final int CDR_FILE_CAPACITY = 10;

    public ConcurrentQueue(List<Msisdns> msisdns, CDRFileSenderService cdrFileSenderService, TransactionsRepository transactionsRepository) {
        this.msisdns = msisdns;
        this.cdrFileSenderService = cdrFileSenderService;
        this.transactionsRepository = transactionsRepository;
    }

    public synchronized void enqueue(List<TransactionObject> transactionObjects) {
        if (!transactionObjects.isEmpty()) {
            for (TransactionObject obj : transactionObjects) {
                if (queue.size() < CDR_FILE_CAPACITY) {
                    queue.add(obj);
                } else {
                    dequeue();
                }
            }
        }
    }

    private void dequeue() {
        Path filePath = Paths.get(ROOT_PATH + "/" + "CDR_File" + "_" + cdrFileCounter + ".txt");
        queue = queue.stream().sorted(Comparator.comparing(TransactionObject::getCallStartTime)).collect(Collectors.toList());
        writeCDRFile(filePath);
        sendCDRFiles(filePath.toFile());
        writeToDataBase(msisdns);
        queue.clear();
        cdrFileCounter++;
    }

    private void writeCDRFile(Path filePath) {
        try {
            if (!Files.exists(ROOT_PATH.toAbsolutePath())) {
                Files.createDirectory(ROOT_PATH.toAbsolutePath());
            }
            Files.deleteIfExists(filePath);
            Path file = Files.createFile(filePath);

            try (FileOutputStream outputStream = new FileOutputStream(file.toFile())) {

                for (TransactionObject transaction : queue) {
                    outputStream.write((transaction.toString() + "\n").getBytes());
                    outputStream.flush();
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "EXCEPTION: " + Arrays.toString(e.getStackTrace()));
        }
    }

    private void writeToDataBase(List<Msisdns> msisdns) {
        List<Transactions> transactions = new ArrayList<>();
        for (TransactionObject transactionObject : queue) {
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

    private void sendCDRFiles(File file) {
        cdrFileSenderService.sendFile(file);
        file.delete();
    }
}
