package brt.brt_service.Tests;

import brt.brt_service.BRTUtils.DataToAddNewUser;
import brt.brt_service.Controllers.NewUserController;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Тесты для NewUserController.
 */
@ExtendWith(MockitoExtension.class)
public class NewUserControllerTest {
    @InjectMocks
    private NewUserController newUserController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(newUserController).build();
    }

    /**
     * Тест без номера телефона абонента.
     */
    @Test
    void catchCallReceiptNoMsisdn() throws Exception {
        DataToAddNewUser dataToAddNewUser = new DataToAddNewUser(null, 11L, 120F,
                "Федор", "Панфилов", "Дмитриевич");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(dataToAddNewUser);
        mockMvc.perform(
                        post("/post-new-user")
                                .header("Content-Type", "application/json")
                                .content(requestJson)

                )
                .andExpect(status().isBadRequest());
    }
}
