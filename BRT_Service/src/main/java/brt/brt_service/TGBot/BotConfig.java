package brt.brt_service.TGBot;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурационный класс для настроек Telegram Bot.
 */
@Configuration
@Data
public class BotConfig {
    /**
     * Имя Telegram бота.
     */
    @Value("${spring.bot.name}")
    String botName;
    /**
     * Имя Telegram бота.
     */
    @Value("${spring.bot.token}")
    String token;
}
