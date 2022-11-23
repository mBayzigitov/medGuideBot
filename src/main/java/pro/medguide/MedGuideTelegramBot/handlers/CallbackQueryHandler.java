package pro.medguide.MedGuideTelegramBot.handlers;

import lombok.AccessLevel;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.io.IOException;

@Component
public class CallbackQueryHandler {

    public BotApiMethod<?> handleCallback(CallbackQuery buttonCommand) throws IOException {

//        String chatID = buttonCommand.getMessage().getChatId().toString();
//
//        String data = buttonCommand.getData();
//
//        switch (data) {
//
//            case "" -> {
//
//            }
//
//        }

        return null;

    }

}
