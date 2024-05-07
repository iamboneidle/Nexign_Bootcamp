package brt.brt_service.TGBot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;

/**
 * Класс, представляющий бота для хранения CDR файлов в Telegram.
 */
@Component
public class CDRFileStorageBot extends TelegramLongPollingBot {
    /**
     * Имя Telegram бота.
     */
    @Value("${spring.bot.name}")
    private String botName;
    /**
     * Имя Telegram бота.
     */
    @Value("${spring.bot.token}")
    private String token;
    /**
     * ID чата.
     */
    private Long chatId = null;

    /**
     * Получает имя бота.
     *
     * @return Имя бота.
     */
    @Override
    public String getBotUsername() {
        return botName;
    }

    /**
     * Получает токен бота.
     *
     * @return Токен бота.
     */
    @Override
    public String getBotToken() {
        return token;
    }

    /**
     * Обработка входящего обновления.
     *
     * @param update Обновление.
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            chatId = update.getMessage().getChatId();
            startCommandReceived(update.getMessage().getChat().getFirstName());
        }
    }

    /**
     * Обработка команды "start".
     *
     * @param name Имя пользователя.
     */
    private void startCommandReceived(String name) {
        String answer = "Привет, " + name + ", сейчас я начну отправлять сюда CDR файлы ʕ⁎̯͡⁎ʔ༄";
        sendMessage(answer);
    }

    /**
     * Отправка сообщения.
     *
     * @param textToSend Текст сообщения.
     */
    public void sendMessage(String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {

        }
    }

    /**
     * Отправка документа.
     *
     * @param file     Файл для отправки.
     * @param filename Имя файла.
     */
    public void sendDocument(File file, String filename) {
        if (chatId != null) {
            SendDocument document = new SendDocument();
            document.setChatId(chatId);
            document.setDocument(new InputFile(file, filename));
            try {
                execute(document);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
