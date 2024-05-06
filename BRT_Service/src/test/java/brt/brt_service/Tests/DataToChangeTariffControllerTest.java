package brt.brt_service.Tests;

import brt.brt_service.BRTUtils.DataToChangeTariff;
import brt.brt_service.Controllers.DataToChangeTariffController;
import brt.brt_service.Postgres.DAO.Repository.MsisdnsRepository;
import brt.brt_service.Postgres.DAO.Repository.RatesRepository;
import brt.brt_service.Redis.DAO.Repository.MsisdnToMinutesLeftRepository;
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
 * Тесты для DataToChangeTariffController.
 */
@ExtendWith(MockitoExtension.class)
public class DataToChangeTariffControllerTest {
    @Mock
    private MsisdnsRepository msisdnsRepository;
    @Mock
    private RatesRepository ratesRepository;
    @Mock
    private MsisdnToMinutesLeftRepository msisdnToMinutesLeftRepository;
    @InjectMocks
    private DataToChangeTariffController dataToChangeTariffController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(dataToChangeTariffController).build();
    }

    /**
     * Тест без ID тарифа.
     */
    @Test
    void catchCallReceiptWithEmptyTariffId() throws Exception {
        DataToChangeTariff dataToChangeTariff = new DataToChangeTariff("79218476904", null);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(dataToChangeTariff);
        mockMvc.perform(
                        post("/change-tariff")
                                .header("Content-Type", "application/json")
                                .content(requestJson)

                )
                .andExpect(status().isNoContent());
    }

    /**
     * Тест без номера телефона абонента.
     */
    @Test
    void catchCallReceiptEmptyMsisdn() throws Exception {
        DataToChangeTariff dataToChangeTariff = new DataToChangeTariff(null, 11L);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(dataToChangeTariff);
        mockMvc.perform(
                        post("/change-tariff")
                                .header("Content-Type", "application/json")
                                .content(requestJson)

                )
                .andExpect(status().isNoContent());
    }
}
