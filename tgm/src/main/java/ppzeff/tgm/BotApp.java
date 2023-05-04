package ppzeff.tgm;

import com.pengrad.telegrambot.TelegramBot;
import ppzeff.tgm.bot.BotProcessor;
import ppzeff.tgm.service.BotServiceImp;
import ppzeff.shared.Constants;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class BotApp {
    public static void main(String[] args) throws IOException, TimeoutException {
         TelegramBot bot = new TelegramBot.Builder(System.getenv("botToken"))
                .updateListenerSleep(1000)
                .build();
        BotProcessor botProcessor = new BotProcessor(bot, new BotServiceImp());
        botProcessor.start();
    }
}
