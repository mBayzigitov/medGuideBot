package pro.medguide.MedGuideTelegramBot.handlers;

import lombok.AccessLevel;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import pro.medguide.MedGuideTelegramBot.telegramData.ReplyKeyboardMaker;

import java.io.File;
import java.io.IOException;

@Component
public class CallbackQueryHandler {

    public BotApiMethod<?> handleCallback(CallbackQuery buttonCommand) throws IOException {

        // PLUG
        return null;

    }

}
