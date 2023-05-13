package ppzeff.tgm.bot.listener.factory;


import ppzeff.tgm.bot.listener.AbstractMessageListener;
import ppzeff.tgm.bot.listener.VendorMessageListener;

public class VendorListenerFactory extends AbstractListenerFactory{
    @Override
    public AbstractMessageListener build(long chatId) {
        return new VendorMessageListener(getBot(), chatId);
    }

    @Override
    public String getInfo() {
        return "устанавливает воставщика услуг";
    }

    @Override
    public String getCommand() {
        return "/задатьВендора";
    }

    @Override
    public boolean flag() {
        return false;
    }
}
