package brt.brt_service.Tests;

import brt.brt_service.BRTUtils.CallReceipt;
import brt.brt_service.Controllers.CallReceiptController;
import brt.brt_service.Services.BRTService;
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
 * Тесты для CallReceiptController.
 */
@ExtendWith(MockitoExtension.class)
public class CallReceiptControllerTest {
    @Mock
    private BRTService brtService;
    @InjectMocks
    private CallReceiptController callReceiptController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(callReceiptController).build();
    }

    /**
     * Тест с валидными данными.
     */
    @Test
    void catchCallReceipt() throws Exception {
        CallReceipt callReceipt = new CallReceipt("79218476904", null, null);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(callReceipt);
        mockMvc.perform(
                        post("/post-call-receipt")
                                .header("Content-Type", "application/json")
                                .content(requestJson)

                )
                .andExpect(status().isOk());
    }

    /**
     * Тест без номера телефона абонента.
     */
    @Test
    void catchCallReceiptEmptyMsisdn() throws Exception {
        CallReceipt callReceipt = new CallReceipt(null, null, null);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(callReceipt);
        mockMvc.perform(
                        post("/post-call-receipt")
                                .header("Content-Type", "application/json")
                                .content(requestJson)

                )
                .andExpect(status().isBadRequest());
    }
}
