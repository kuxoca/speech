package ppzeff.tgm.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import lombok.extern.slf4j.Slf4j;
import ppzeff.tgm.dto.UserDetail;
import ppzeff.tgm.service.UserDetailService;

@Slf4j
public class VendorMessageListener extends AbstractMessageListener {
    String command = "/задатьВендора";

    final UserDetailService userDetailService;

    @Override
    public void onMessage(Message message) {
        log.info("Invoking text: {}", message.text());
        var commandParam = message.text().split(" ")[1];
        userDetailService.setUserDetail(
                message.chat().id(),
                UserDetail.builder()
                        .vendor(commandParam)
//                        .lang("Ru-ru")
                        .build());
        sendMessage(String.format("текущий параметр: %s", userDetailService.getVendorByUserId(message.chat().id())));
    }

    public VendorMessageListener(TelegramBot bot, long chatId) {
        super(bot, chatId);
        this.userDetailService = UserDetailService.getInstance();
    }

    @Override
    public boolean invocation(Message message) {
        var text = message.text();
        return text.startsWith(command);
    }

}
