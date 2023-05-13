package ppzeff.tgm.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.File;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Voice;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetFileResponse;
import lombok.extern.slf4j.Slf4j;
import ppzeff.tgm.service.UserDetailService;
import ppzeff.tgm.service.UserDetailServiceImp;
import ppzeff.tgm.service.BotProcessor;
import ppzeff.tgm.service.BotProcessorImp;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.concurrent.TimeoutException;

@Slf4j
public class VoiceMessageListener extends AbstractMessageListener {
    UserDetailService userDetailService = UserDetailServiceImp.getInstance();
    private final BotProcessor botProcessor;

    @Override
    public void onMessage(Message message) {
        var vendor = userDetailService.getVendorByUserId(message.from().id());
//        log.info("voice vendor {}", vendor);
        var byteFromVoice = getByteFromVoice(message.voice());

        String recognizeText = botProcessor.sendAndGetText(byteFromVoice, vendor);
        sendMessage(
                new SendMessage(message.chat().id(), recognizeText)
                        .replyToMessageId(message.messageId())
        );
    }

    public VoiceMessageListener(TelegramBot bot, long chatId) {
        super(bot, chatId);
        try {
            botProcessor = new BotProcessorImp();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean invocation(Message message) {
        return message.voice() != null;
    }

    private byte[] getByteFromVoice(Voice voice) {
        GetFile getFile = new GetFile(voice.fileId());
        GetFileResponse getFileResponse = getBot().execute(getFile);
        File file = getFileResponse.file();
        String fullPath = getBot().getFullFilePath(file);

        try (InputStream inputStream = new URL(fullPath).openStream()) {
            return inputStream.readAllBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
