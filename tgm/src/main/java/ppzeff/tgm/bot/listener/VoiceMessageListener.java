package ppzeff.tgm.bot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.User;

public class VoiceMessageListener extends AbstractMessageListener {
    @Override
    public void onMessage(Message message) {
        User user = message.from();
        user.id();
    }

    public VoiceMessageListener(TelegramBot bot, long chatId) {
        super(bot, chatId);
    }

    @Override
    public boolean invocation(Message message) {
        return message.voice() != null;
    }

}
