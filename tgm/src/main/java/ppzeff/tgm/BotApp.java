package ppzeff.tgm;

import lombok.extern.slf4j.Slf4j;
import ppzeff.tgm.bot.BotService;
import ppzeff.tgm.bot.listener.factory.SettingListenerFactory;
import ppzeff.tgm.bot.listener.factory.VendorListenerFactory;
import ppzeff.tgm.bot.listener.factory.VoiceListenerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Slf4j
public class BotApp {
    public static void main(String[] args) {
        BotService botService;
        try {
            botService = BotService.getInstance();
            botService.registerFactories(
                    new VoiceListenerFactory(),
                    new SettingListenerFactory(),
                    new VendorListenerFactory()
            );
            log.info("Wait message... https://t.me/DCSRM2_bot");
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
