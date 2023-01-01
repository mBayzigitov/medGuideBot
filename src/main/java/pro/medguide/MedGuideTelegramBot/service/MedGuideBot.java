package pro.medguide.MedGuideTelegramBot.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import pro.medguide.MedGuideTelegramBot.handlers.CallbackQueryHandler;
import pro.medguide.MedGuideTelegramBot.handlers.MessageHandler;

import java.io.IOException;
import java.util.HashMap;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedGuideBot extends SpringWebhookBot {

    String botPath, botUsername, botToken;

    MessageHandler messageHandler;
    CallbackQueryHandler callbackQueryHandler;

    private static HashMap<String, String> usersStates;

    public MedGuideBot(SetWebhook setWebhook, MessageHandler messageHandler, CallbackQueryHandler callbackQueryHandler) {
        super(setWebhook);
        this.messageHandler = messageHandler;
        this.callbackQueryHandler = callbackQueryHandler;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
            return handleUpdate(update);
        } catch (Exception e) {
            return new SendMessage(update.getMessage().getChatId().toString(),
                    "Exception handling TODO"); // TODO
        }
    }

    private BotApiMethod<?> handleUpdate(Update update) throws IOException {

        // Handling buttons pressing
        if (update.hasCallbackQuery()) {
            return callbackQueryHandler.handleCallback(update.getCallbackQuery());
        }

        // Handling commands or usual messages
        if (update.hasMessage()) {

            // if command
            if (update.getMessage().hasEntities()) {

                return messageHandler.handleCommand(update.getMessage());

            } else {

                return messageHandler.handleMessage(update.getMessage());

            }

        }

        // PLUG
        return null;

    }
}
