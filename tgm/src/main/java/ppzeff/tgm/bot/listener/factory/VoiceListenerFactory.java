package ppzeff.tgm.bot.listener.factory;


import ppzeff.tgm.bot.listener.AbstractMessageListener;
import ppzeff.tgm.bot.listener.VoiceMessageListener;

public class VoiceListenerFactory extends AbstractListenerFactory{

    @Override
    public AbstractMessageListener build(long chatId) {
        return new VoiceMessageListener(getBot(), chatId);
    }

    @Override
    public String getInfo() {
        return "при получении голосового сообщения в ответ отправляется текстовая транскрипция";
    }

    @Override
    public String getCommand() {
        return "onGet voice message";
    }

    @Override
    public boolean flag() {
        return false;
    }
}
