package cdr.cdr_service.Services;

import cdr.cdr_service.CDRUtils.CDRUser;
import cdr.cdr_service.CDRUtils.TransactionObject;
import cdr.cdr_service.DAO.Models.Msisdns;
import cdr.cdr_service.DAO.Models.Transactions;
import cdr.cdr_service.DAO.Repository.MsisdnsRepository;
import cdr.cdr_service.DAO.Repository.TransactionsRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;

@Service
public class CDRService {
    @Autowired
    private MsisdnsService msisdnsService;
    @Autowired
    private MsisdnsRepository msisdnsRepository;
    @Autowired
    private TransactionsRepository transactionsRepository;

    @PostConstruct
    public void init() throws InterruptedException {
        List<Msisdns> msisdns = msisdnsService.getMsisdns();
        ExecutorService executor = Executors.newFixedThreadPool(msisdns.size());
        List<Future<List<TransactionObject>>> futures = new ArrayList<>();
        for (int monthNum = 1; monthNum <= 12; monthNum++) {
            CountDownLatch latch = new CountDownLatch(msisdns.size());
            int finalMonthNum = monthNum;
            msisdns.forEach(msisdn -> futures.add(executor.submit(new CDRUser(msisdn.getPhoneNumber(), finalMonthNum, latch))));
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
        }
        executor.shutdown();
    }

    private void writeToDataBase(List<Msisdns> msisdns, List<TransactionObject> transactionObjectsForMonth) {
        List<Transactions> transactions = new ArrayList<>();
        for (TransactionObject transactionObject : transactionObjectsForMonth) {
            Msisdns toPut = null;
            for (Msisdns msisdn : msisdns) {
                if (msisdn.getPhoneNumber().equals(transactionObject.getServicedMsisdn())) {
                    toPut = msisdn;
                }
            }
            transactions.add(new Transactions(
                    toPut,
                    msisdnsService.getIdByMsisdns(transactionObject.getServicedMsisdn()),
                    transactionObject.getCallType(),
                    transactionObject.getContactedMsisdn(),
                    transactionObject.getCallStartTime(),
                    transactionObject.getCallEndTime()
            ));
        }
        transactionsRepository.saveAll(transactions);
    }

    private void makeCdrFiles(List<TransactionObject> transactionObjectsForMonth) {}

    private void sendCdrFiles() {}
}
