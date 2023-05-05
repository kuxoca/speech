package ppzeff.tgm;

import com.pengrad.telegrambot.TelegramBot;
import ppzeff.tgm.bot.BotService;
import ppzeff.tgm.service.BotProcessorImp;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class BotApp {
    public static void main(String[] args) throws IOException, TimeoutException {
         TelegramBot bot = new TelegramBot.Builder(System.getenv("botToken"))
                .updateListenerSleep(1000)
                .build();
        BotService botService = new BotService(bot, new BotProcessorImp());
        botService.start();
    }
}
