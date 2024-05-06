package brt.brt_service.Tests;

import brt.brt_service.BRTUtils.DataToPutMoney;
import brt.brt_service.Controllers.PutMoneyOnAccountsController;
import brt.brt_service.Postgres.DAO.Repository.MsisdnsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Тесты для PutMoneyOnAccountsController.
 */
@ExtendWith(MockitoExtension.class)
public class PutMoneyOnAccountsControllerTest {
    @Mock
    private MsisdnsRepository msisdnsRepository;
    @InjectMocks
    private PutMoneyOnAccountsController putMoneyOnAccountsController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(putMoneyOnAccountsController).build();
    }

    /**
     * Тест с валидными данными.
     */
    @Test
    void putMoney() throws Exception {
        DataToPutMoney dataToPutMoney = new DataToPutMoney("79218476904", 120F);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(dataToPutMoney);
        mockMvc.perform(
                        post("/put-money-on-accounts")
                                .header("Content-Type", "application/json")
                                .content(requestJson)

                )
                .andExpect(status().isOk());
    }

    /**
     * Тест без номера телефона абонента.
     */
    @Test
    void putMoneyNoMsisdn() throws Exception {
        DataToPutMoney dataToPutMoney = new DataToPutMoney(null, 120F);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(dataToPutMoney);
        mockMvc.perform(
                        post("/put-money-on-accounts")
                                .header("Content-Type", "application/json")
                                .content(requestJson)

                )
                .andExpect(status().isNoContent());
    }

    /**
     * Тест без данных о количестве денег.
     */
    @Test
    void putMoneyNoMoney() throws Exception {
        DataToPutMoney dataToPutMoney = new DataToPutMoney("79218476904", null);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(dataToPutMoney);
        mockMvc.perform(
                        post("/put-money-on-accounts")
                                .header("Content-Type", "application/json")
                                .content(requestJson)

                )
                .andExpect(status().isNoContent());
    }

    /**
     * Тест с пустыми данными.
     */
    @Test
    void putMoneyNoData() throws Exception {
        DataToPutMoney dataToPutMoney = new DataToPutMoney(null, null);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(dataToPutMoney);
        mockMvc.perform(
                        post("/put-money-on-accounts")
                                .header("Content-Type", "application/json")
                                .content(requestJson)

                )
                .andExpect(status().isNoContent());
    }
}
