package hrs.hrs_service.Tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import hrs.hrs_service.Controllers.DataToPayController;
import hrs.hrs_service.HRSUtils.DataToPay;
import hrs.hrs_service.Services.HRSService;
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
 * Тесты для DataToPayController.
 */
@ExtendWith(MockitoExtension.class)
public class DataToPayControllerTest {
    @Mock
    private HRSService hrsService;
    @InjectMocks
    private DataToPayController dataToPayController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(dataToPayController).build();
    }

    /**
     * Тест с валидными данными.
     */
    @Test
    void catchDataToPay() throws Exception{
        DataToPay dataToPay = new DataToPay("01", "79218476904", 1L, 2L, 11, true, null, null);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(dataToPay);
        mockMvc.perform(
                        post("/post-data-to-pay")
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
        DataToPay dataToPay = new DataToPay("01", null, 1L, 2L, 11, true, null, null);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(dataToPay);
        mockMvc.perform(
                        post("/post-data-to-pay")
                                .header("Content-Type", "application/json")
                                .content(requestJson)

                )
                .andExpect(status().isBadRequest());
    }
}
