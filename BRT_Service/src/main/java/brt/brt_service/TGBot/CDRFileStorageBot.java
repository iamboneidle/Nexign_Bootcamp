package brt.brt_service.TGBot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;

@Component
public class CDRFileStorageBot extends TelegramLongPollingBot {
    @Autowired
    private BotConfig botConfig;
    private Long chatId = null;

    @Override
    public String getBotUsername() {
        return botConfig.botName;
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            chatId = update.getMessage().getChatId();
            startCommandReceived(update.getMessage().getChat().getFirstName());
        }
    }

    public CDRFileStorageBot(BotConfig botConfig) {
        this.botConfig = botConfig;
    }

    private void startCommandReceived(String name) {
        String answer = "Привет, " + name + ", сейчас я начну отправлять сюда CDR файлы ʕ⁎̯͡⁎ʔ༄";
        sendMessage(answer);
    }

    public void sendMessage(String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {

        }
    }

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
