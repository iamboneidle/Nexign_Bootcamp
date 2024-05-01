package crm.crm_service.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import crm.crm_service.CRMExecutors.RequestExecutor;
import crm.crm_service.CRMUtils.DataToAddNewUser;
import crm.crm_service.CRMUtils.DataToAddNewUserToCDR;
import crm.crm_service.DAO.Models.Users;
import crm.crm_service.DAO.Repository.UsersRepository;
import jakarta.annotation.PostConstruct;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Класс, отвечающий за добавление нового пользователя во все сервисы.
 */
@Service
public class NewUserAdderService {
    /**
     * Репозиторйи пользователей.
     */
    @Autowired
    private UsersRepository usersRepository;
    /**
     * CRMService.
     */
    @Autowired
    private CRMService crmService;
    /**
     * Объект ObjectMapper для преобразования объектов в Json.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();
    /**
     * Класс, отправляющий запросы.
     */
    private final RequestExecutor requestExecutor = new RequestExecutor();
    /**
     * Клиент.
     */
    private OkHttpClient client;
    /**
     * Роль пользователя.
     */
    private static final String USER_ROLE = "SUBSCRIBER";
    /**
     * Пароль пользователя.
     */
    private static final String USER_PASSWORD = "$2a$10$/KNnNb5iScV/sWinUSV5m.HRh.snnqfNJ3RYBwuEtuM9Pzo2fVhUG";
    /**
     * URL-адрес контроллера в CDR, ожидающего данные о добавлении нового пользователя.
     */
    private static final String POST_TO_CDR_URL = "http://localhost:2001/post-new-user";
    /**
     * URL-адрес контроллера в BRT, ожидающего данные о добавлении нового пользователя.
     */
    private static final String POST_TO_BRT_URL = "http://localhost:2002/post-new-user";

    /**
     * Метод инициализирующий клиента на PostConstruct.
     */
    @PostConstruct
    private void init() {
        client = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool())
                .writeTimeout(40000, TimeUnit.MILLISECONDS)
                .readTimeout(40000, TimeUnit.MILLISECONDS)
                .build();
    }

    /**
     * Метод для добавления пользователей во сей сервисы.
     *
     * @param dataToAddNewUser Данные для добавления нового пользователя.
     */
    public void add(DataToAddNewUser dataToAddNewUser) {
        addToCRM(dataToAddNewUser);
        addToBRT(dataToAddNewUser);
        addToCDR(new DataToAddNewUserToCDR(dataToAddNewUser.getMsisdn()));
    }

    /**
     * Метод для добавления нового пользователя в CRM сервис.
     *
     * @param dataToAddNewUser Данные для добавления нового пользователя.
     */
    private void addToCRM(DataToAddNewUser dataToAddNewUser) {
        usersRepository.save(new Users(dataToAddNewUser.getMsisdn(), USER_PASSWORD, USER_ROLE));
        crmService.getMapNumberToRateId().put(dataToAddNewUser.getMsisdn(), dataToAddNewUser.getTariffId());
    }

    /**
     * Метод для добавления нового пользователя в BRT сервис.
     *
     * @param dataToAddNewUser Данные для добавления нового пользователя.
     */
    private void addToBRT(DataToAddNewUser dataToAddNewUser) {
        try {
            String json = objectMapper.writeValueAsString(dataToAddNewUser);
            requestExecutor.execute(json, POST_TO_BRT_URL, client);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Метод для добавления нового пользователя в CDR сервис.
     *
     * @param dataToAddNewUserToCDR Данные для добавления нового пользователя в CDR.
     */
    private void addToCDR(DataToAddNewUserToCDR dataToAddNewUserToCDR) {
        try {
            String json = objectMapper.writeValueAsString(dataToAddNewUserToCDR);
            requestExecutor.execute(json, POST_TO_CDR_URL, client);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
