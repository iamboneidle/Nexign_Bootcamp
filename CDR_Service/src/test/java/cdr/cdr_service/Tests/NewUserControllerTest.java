package cdr.cdr_service.Tests;

import cdr.cdr_service.CDRUtils.DataToAddNewUserToCDR;
import cdr.cdr_service.Controllers.NewUserController;
import cdr.cdr_service.Services.CDRService;
import com.fasterxml.jackson.core.JsonProcessingException;
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
 * Тесты для NewUserController.
 */
@ExtendWith(MockitoExtension.class)
public class NewUserControllerTest {
    @Mock
    private CDRService cdrService;
    @InjectMocks
    private NewUserController newUserController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(newUserController).build();
    }

    /**
     * Тест для контроллера по добавлению нового пользователя, если данные валидны.
     */
    @Test
    void addNewUser() throws Exception {
        DataToAddNewUserToCDR dataToAddNewUser = new DataToAddNewUserToCDR("79218476904");
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(dataToAddNewUser);
        mockMvc.perform(
                        post("/post-new-user")
                                .header("Content-Type", "application/json")
                                .content(requestJson)

                )
                .andExpect(status().isOk());
    }

    /**
     * Тест для контроллера по добавлению нового пользователя, если данные пусты.
     */
    @Test
    void addNewUserWithEmptyData() throws Exception {
        DataToAddNewUserToCDR dataToAddNewUser = new DataToAddNewUserToCDR();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(dataToAddNewUser);
        mockMvc.perform(
                        post("/post-new-user")
                                .header("Content-Type", "application/json")
                                .content(requestJson)

                )
                .andExpect(status().isNoContent());
    }
}
