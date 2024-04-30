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

@Service
public class NewUserAdderService {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private CRMService crmService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RequestExecutor requestExecutor = new RequestExecutor();
    private OkHttpClient client;
    private static final String USER_ROLE = "SUBSCRIBER";
    private static final String USER_PASSWORD = "$2a$10$/KNnNb5iScV/sWinUSV5m.HRh.snnqfNJ3RYBwuEtuM9Pzo2fVhUG";
    private static final String POST_TO_CDR_URL = "http://localhost:2001/post-new-user";
    private static final String POST_TO_BRT_URL = "http://localhost:2002/post-new-user";

    @PostConstruct
    private void init() {
        client = new OkHttpClient.Builder()
                .connectionPool(new ConnectionPool())
                .writeTimeout(40000, TimeUnit.MILLISECONDS)
                .readTimeout(40000, TimeUnit.MILLISECONDS)
                .build();
    }

    public void add(DataToAddNewUser dataToAddNewUser) {
        addToCRM(dataToAddNewUser);
        addToBRT(dataToAddNewUser);
        addToCDR(new DataToAddNewUserToCDR(dataToAddNewUser.getMsisdn()));
    }

    private void addToCRM(DataToAddNewUser dataToAddNewUser) {
        usersRepository.save(new Users(dataToAddNewUser.getMsisdn(), USER_PASSWORD, USER_ROLE));
        crmService.getMapNumberToRateId().put(dataToAddNewUser.getMsisdn(), dataToAddNewUser.getTariffId());
    }

    private void addToBRT(DataToAddNewUser dataToAddNewUser) {
        try {
            String json = objectMapper.writeValueAsString(dataToAddNewUser);
            requestExecutor.execute(json, POST_TO_BRT_URL, client);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void addToCDR(DataToAddNewUserToCDR dataToAddNewUserToCDR) {
        try {
            String json = objectMapper.writeValueAsString(dataToAddNewUserToCDR);
            requestExecutor.execute(json, POST_TO_CDR_URL, client);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
