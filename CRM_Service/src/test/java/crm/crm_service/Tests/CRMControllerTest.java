package crm.crm_service.Tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import crm.crm_service.CRMUtils.DataToAddNewUser;
import crm.crm_service.CRMUtils.DataToChangeTariff;
import crm.crm_service.CRMUtils.DataToPutMoney;
import crm.crm_service.Controllers.CRMController;
import crm.crm_service.Services.CRMService;
import crm.crm_service.Services.DataToChangeTariffSenderService;
import crm.crm_service.Services.DataToPutMoneySenderService;
import crm.crm_service.Services.NewUserAdderService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Тесты для CRMController.
 */
@ExtendWith(MockitoExtension.class)
public class CRMControllerTest {
    @Mock
    private CRMService crmService;
    @Mock
    private DataToChangeTariffSenderService dataToChangeTariffSenderService;
    @Mock
    private DataToPutMoneySenderService dataToPutMoneySenderService;
    @Mock
    private NewUserAdderService newUserAdderService;
    @InjectMocks
    private CRMController crmController;
    private MockMvc mockMvc;
    private static final String USER_PASSWORD = "user";
    private static final String ADMIN_LOGIN = "admin";
    private static final String ADMIN_PASSWORD = "admin";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(crmController).build();
    }

    //Тесты для end-point'ов админа.
    /**
     * Тест для проверки правильности работы контроллера по добавлению нового пользователя,
     * если все данные валидны.
     */
    @Test
    void save() throws Exception {
        DataToAddNewUser dataToAddNewUser = new DataToAddNewUser("79218476904", 11L, 120F,
                "Федор", "Панфилов", "Дмитриевич");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(dataToAddNewUser);
        mockMvc.perform(
                        post("/admin/save")
                                .header("Authorization", getBasicAuthenticationHeader(ADMIN_LOGIN, ADMIN_PASSWORD))
                                .header("Content-Type", "application/json")
                                .content(requestJson)

                )
                .andExpect(status().isOk());
    }

    /**
     * Тест для проверки контроллера на поступление пустых данных.
     */
    @Test
    void saveEmptyUser() throws Exception {
        DataToAddNewUser dataToAddNewUser = new DataToAddNewUser();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(dataToAddNewUser);
        mockMvc.perform(
                        post("/admin/save")
                                .header("Authorization", getBasicAuthenticationHeader(ADMIN_LOGIN, ADMIN_PASSWORD))
                                .header("Content-Type", "application/json")
                                .content(requestJson)

                )
                .andExpect(status().isNoContent());
    }

    /**
     * Тест контроллера, сменяющего тариф пользователю, если данные валидны.
     */
    @Test
    void changeTariffs() throws Exception {
        DataToChangeTariff dataToChangeTariff = new DataToChangeTariff("79218476904", 12L);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(dataToChangeTariff);
        mockMvc.perform(
                        post("/admin/change-tariff")
                                .header("Authorization", getBasicAuthenticationHeader(ADMIN_LOGIN, ADMIN_PASSWORD))
                                .header("Content-Type", "application/json")
                                .content(requestJson)

                )
                .andExpect(status().isOk());
    }

    /**
     * Тест для проверки контроллера, сменяющего тариф, если данные пусты.
     */
    @Test
    void changeTariffsWithoutContent() throws Exception {
        mockMvc.perform(
                        post("/admin/change-tariff")
                                .header("Content-Type", "application/json")
                                .header("Authorization", getBasicAuthenticationHeader(ADMIN_LOGIN, ADMIN_PASSWORD))
                                .content("{}")

                )
                .andExpect(status().isNoContent());
    }

    //Тесты для end-point'ов пользователей.
    /**
     * Тест дл проверки контроллера, который кладет деньги пользователю на счет.
     */
    @Test
    void putMoney() throws Exception {
        DataToPutMoney dataToPutMoney = new DataToPutMoney("79218476904", 100F);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(dataToPutMoney);
        mockMvc.perform(
                        post("/user/put-money")
                                .header("Content-Type", "application/json")
                                .header("Authorization", getBasicAuthenticationHeader(dataToPutMoney.getMsisdn(), USER_PASSWORD))
                                .content(requestJson)
                )
                .andExpect(status().isOk());
    }

    /**
     * Тест для проверки контроллера, если номер авторизованного абонента не совпадает с номером
     * абонента, на который пользователь хочет положить деньги.
     */
    @Test
    void putMoneyWrongMsisdn() throws Exception {
        DataToPutMoney dataToPutMoney = new DataToPutMoney("79218476904", 100F);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(dataToPutMoney);
        mockMvc.perform(
                        post("/user/put-money")
                                .header("Content-Type", "application/json")
                                .header("Authorization", getBasicAuthenticationHeader("79210000000", USER_PASSWORD))
                                .content(requestJson)
                )
                .andExpect(status().isConflict());
    }

    /**
     * Тест контроллера для пополнения баланса, если на вход приходят пустые данне.
     */
    @Test
    void putMoneyEmptyData() throws Exception {
        DataToPutMoney dataToPutMoney = new DataToPutMoney();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(dataToPutMoney);
        mockMvc.perform(
                        post("/user/put-money")
                                .header("Content-Type", "application/json")
                                .header("Authorization", getBasicAuthenticationHeader("79210000000", USER_PASSWORD))
                                .content(requestJson)
                )
                .andExpect(status().isNoContent());
    }

    /**
     * Метод, конвертации данных авторизации в Basic как в CRMController.
     */
    @NotNull
    private String getBasicAuthenticationHeader(String username, String password) {
        String valueToEncode = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
    }
}
