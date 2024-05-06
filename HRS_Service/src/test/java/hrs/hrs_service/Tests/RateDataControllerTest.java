package hrs.hrs_service.Tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import hrs.hrs_service.Controllers.RateDataController;
import hrs.hrs_service.DAO.Repository.RatesRepository;
import hrs.hrs_service.HRSUtils.RateData;
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
 * Тесты для RateDataController.
 */
@ExtendWith(MockitoExtension.class)
public class RateDataControllerTest {
    @Mock
    private RatesRepository ratesRepository;
    @InjectMocks
    private RateDataController rateDataController;
    private MockMvc mockMvc;
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(rateDataController).build();
    }

    /**
     * Тест с валидными данными.
     */
    @Test
    void catchDataToPay() throws Exception{
        RateData rateData = new RateData(23L, "test rate", null, 50L, 1f, 2f, 0f,0f);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(rateData);
        mockMvc.perform(
                        post("/post-rate-data")
                                .header("Content-Type", "application/json")
                                .content(requestJson)

                )
                .andExpect(status().isOk());
    }

    /**
     * Тест с невалидными данными.
     */
    @Test
    void catchDataToPayEmptyRateName() throws Exception{
        RateData rateData = new RateData(23L, null, null, 50L, 1f, 2f, 0f,0f);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(rateData);
        mockMvc.perform(
                        post("/post-rate-data")
                                .header("Content-Type", "application/json")
                                .content(requestJson)

                )
                .andExpect(status().isBadRequest());
    }
}
