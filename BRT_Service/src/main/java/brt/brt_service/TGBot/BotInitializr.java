package brt.brt_service.TGBot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.meta.generics.TelegramBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Компонент для инициализации Telegram бота при запуске приложения.
 */
@Component
public class BotInitializr {
    /**
     * Telegram бот, который будет зарегистрирован при инициализации.
     */
    @Autowired
    private TelegramBot telegramBot;
    /**
     * Логгер, выводящий уведомления.
     */
    private static final Logger LOGGER = Logger.getLogger(BotInitializr.class.getName());

    /**
     * Метод инициализации Telegram бота при запуске приложения.
     *
     * @throws TelegramApiException если возникает ошибка при регистрации бота
     */
    @EventListener({ContextRefreshedEvent.class})
    public void init()throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try{
            telegramBotsApi.registerBot((LongPollingBot) telegramBot);
        } catch (TelegramApiException e){
            LOGGER.log(Level.SEVERE, "EXCEPTION: Couldn't initialize Telegram bot");
        }
    }
}
