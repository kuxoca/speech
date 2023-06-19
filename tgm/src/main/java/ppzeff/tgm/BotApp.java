package ppzeff.tgm;

import lombok.extern.slf4j.Slf4j;
import ppzeff.tgm.service.BotServiceImp;
import ppzeff.tgm.listener.factory.SettingListenerFactory;
import ppzeff.tgm.listener.factory.VendorListenerFactory;
import ppzeff.tgm.listener.factory.VoiceListenerFactory;
import ppzeff.tgm.service.BotService;

@Slf4j
public class BotApp {
    public static void main(String[] args) {
        BotService botServiceImp;
        try {
            botServiceImp = BotServiceImp.getInstance();
            botServiceImp.registerFactories(
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
