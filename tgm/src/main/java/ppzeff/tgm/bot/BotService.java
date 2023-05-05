package ppzeff.tgm.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import lombok.extern.slf4j.Slf4j;
import ppzeff.shared.Constants;
import ppzeff.tgm.service.BotProcessor;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Slf4j
public class BotService {
    private final TelegramBot bot;
    private final BotProcessor botProcessor;

    public BotService(TelegramBot bot, BotProcessor botProcessor) {
        this.bot = bot;
        this.botProcessor = botProcessor;
    }

    public void start() {
        log.info("Wait message... https://t.me/dlfyt_bot");
        bot.setUpdatesListener((updates) -> {
            for (Update update : updates) {
                new Thread(() -> {
                    processor(update);
                }).start();
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void processor(Update update) {
        Message message = update.message();
        try {
            if (message != null) {
                Voice voice = message.voice();
                if (voice != null) {
                    byte[] byteFromVoice = getByteFromVoice(voice);
                    String recognizeText = botProcessor.sendAndGetText(byteFromVoice, getTypeService(message.from().id()));
                    replayRecognizeText(recognizeText, message);
                }
            }
        } catch (RuntimeException e) {
            bot.execute(
                    new SendMessage(
                            message.chat().id(),
                            "Произошла ошибка"
                    )
                            .replyToMessageId(message.messageId())
            );
        }
    }

    private void replayRecognizeText(String text, Message message) {
        bot.execute(
                new SendMessage(
                        message.chat().id(),
                        text
                )
                        .disableNotification(true)
                        .parseMode(ParseMode.HTML)
                        .replyToMessageId(message.messageId())
        );
    }

    private String getTypeService(Long id) {
        if (id == 189632375L) {
            return Constants.ROUTING_KEY_SBER;
        } else if (id == 189632385L) {
            return Constants.ROUTING_KEY_YANDEX;
        }
        return Constants.ROUTING_KEY_SBER;
    }

    private byte[] getByteFromVoice(Voice voice) {
        GetFile getFile = new GetFile(voice.fileId());
        GetFileResponse getFileResponse = bot.execute(getFile);
        File file = getFileResponse.file();
        String fullPath = bot.getFullFilePath(file);

        try (InputStream inputStream = new URL(fullPath).openStream()) {
            return inputStream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
