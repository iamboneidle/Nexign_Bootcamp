package brt.brt_service.TGBot;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class BotConfig {
    @Value("${spring.bot.name}")
    String botName;
    @Value("${spring.bot.token}")
    String token;
}
