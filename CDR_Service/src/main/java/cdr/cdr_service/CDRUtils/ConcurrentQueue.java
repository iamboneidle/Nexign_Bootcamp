package cdr.cdr_service.CDRUtils;

import cdr.cdr_service.DAO.Models.Msisdns;
import cdr.cdr_service.DAO.Models.Transactions;
import cdr.cdr_service.DAO.Repository.TransactionsRepository;
import cdr.cdr_service.Kafka.KafkaProducer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
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

/**
 * Этот класс представляет собой конкурентную очередь, в которую попадают транзакции абонентов,
 * затем при заполнении очереди 10-ю транзакциями вызывается метода dequeue, генерируется CDR файл
 * и отправляется в BRT. В программе объект этого класса представлен в единственном экземпляре в классе
 * CDRService, так как очередь одна на все потоки.
 */
public class ConcurrentQueue {
    /**
     * Список, который представляет собой как бы ту самую очередь.
     */
    private List<TransactionObject> queue = new ArrayList<>();
    /**
     * Переменная, которая считает количество отправленных CDR файлов, нужна для того, чтобы класть ее в название
     * каждого CDR файла, чтобы имя последующего было на 1 больше имени предыдущего.
     */
    private long cdrFileCounter = 1;
    /**
     * Репозиторий транзакций, нужен здесь, чтобы сохранять данные о транзакциях пользователей в базу данных
     * CDR сервиса.
     */
    private final TransactionsRepository transactionsRepository;
    /**
     * Кафка продюсер.
     */
    private final KafkaProducer kafkaProducer;
    /**
     * Объект ObjectMapper для преобразования объекта в Json.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();
    /**
     * Список всей абонентов, он здесь нужен для того, чтобы сохранять
     * транзакцию пользователя за самим пользователем.
     */
    private final List<Msisdns> msisdns;
    /**
     * Путь, к папке, в которую каждый новый файл записывается, а затем из нее удаляется после отправки.
     */
    private final Path ROOT_PATH = Paths.get("src/main/resources/CDRFiles").toAbsolutePath();
    /**
     * Логгер для вывода уведомлений в консоль.
     */
    private static final Logger LOGGER = Logger.getLogger(ConcurrentQueue.class.getName());
    /**
     * Переменная, которая содержит параметр, отображающий вместимость CDR файла, именно по ней мы
     * вызываем метод dequeue и отправляем файл, который должен содержать не более 10 записей в себе.
     */
    private static final int CDR_FILE_CAPACITY = 10;

    /**
     * Конструктор класса.
     *
     * @param msisdns                Список всех абонентов.
     * @param kafkaProducer          Кафка продюсер.
     * @param transactionsRepository Репозиторий транзакций.
     */
    public ConcurrentQueue(List<Msisdns> msisdns,
                           TransactionsRepository transactionsRepository,
                           KafkaProducer kafkaProducer
    ) {
        this.msisdns = msisdns;
        this.transactionsRepository = transactionsRepository;
        this.kafkaProducer = kafkaProducer;
    }

    /**
     * Синхронизированный с потоками метод, который проверяет, не пуст ли список с транзакциями абонента, далее
     * проходится по этому списку, если тот не пуст, на каждой итерации цикла сравнивает свой размер с
     * CDR_FILE_CAPACITY (максимально допустимым), если он меньше CDR_FILE_CAPACITY, то мы кладем в очередь
     * еще одну транзакцию, если условие не выполняется, то вызывается метод dequeue().
     *
     * @param transactionObjects Список транзакций абонента за один день.
     */
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

    /**
     * Метод, который делает очистку конкурентной очереди от транзакций. Сначала он генерирует имя и путь нового
     * CDR файла, затем сортирует очередь по времени начала звонка, затем вызывается метод writeCDRFile(filePath)
     * [записать CDR файл], потом sendCDRFiles(filePath.toFile()) [отправить CDR файл в BRT], затем
     * writeToDataBase(msisdns) [записать транзакцию в базу данный на абонента]. Потом очищается очередь
     * и инкриминируется cdrFileCounter.
     */
    private void dequeue() {
        Path filePath = Paths.get(ROOT_PATH + "/" + "CDR_File" + "_" + cdrFileCounter + ".txt");
        queue = queue.stream().sorted(Comparator.comparing(TransactionObject::getCallStartTime)).collect(Collectors.toList());
        writeCDRFile(filePath);
        sendCDRFiles(filePath.toFile());
        writeToDataBase();
        queue.clear();
        cdrFileCounter++;
    }

    /**
     * Метод, который записывает CDR файл, проверяя на всякий случай, существует ли уже какой-то файл с таким
     * же названием.
     *
     * @param filePath Путь по которому записываем файл.
     */
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

    /**
     * Метод для записи транзакций абонентов в базу данных, закрепляя транзакцию за конкретным абонентом.
     */
    private void writeToDataBase() {
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

    /**
     * Метод, отправляющий CDR файл в Kafka.
     *
     * @param file Файл, который отправляем.
     */
    private void sendCDRFiles(File file) {
        String fileName = file.toString().substring(file.toString().lastIndexOf("/") + 1);
        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            StringBuilder content = new StringBuilder();

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line).append("\n");
            }
            String fileContent = content.toString();
            String json = objectMapper.writeValueAsString(new CDRFileToKafka(fileName, fileContent));
            kafkaProducer.sendMessage(json);
            LOGGER.log(Level.INFO, "OK: sent " + fileName + " to Kafka");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "EXCEPTION: " + Arrays.toString(e.getStackTrace()));
        }
    }
}
