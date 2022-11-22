package pro.medguide.MedGuideTelegramBot.service;

import lombok.Data;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import pro.medguide.MedGuideTelegramBot.handlers.CallbackQueryHandler;
import pro.medguide.MedGuideTelegramBot.handlers.MessageHandler;

@Data
public class MedGuideBot extends SpringWebhookBot {

    String botPath, botUsername, botToken;

    MessageHandler messageHandler;
    CallbackQueryHandler callbackQueryHandler;

    public MedGuideBot(SetWebhook setWebhook, MessageHandler messageHandler, CallbackQueryHandler callbackQueryHandler) {
        super(setWebhook);
        this.messageHandler = messageHandler;
        this.callbackQueryHandler = callbackQueryHandler;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return handleUpdate(update);
    }
}
