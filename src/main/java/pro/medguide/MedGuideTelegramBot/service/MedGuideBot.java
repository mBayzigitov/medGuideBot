package pro.medguide.MedGuideTelegramBot.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;
import pro.medguide.MedGuideTelegramBot.handlers.CallbackQueryHandler;
import pro.medguide.MedGuideTelegramBot.handlers.MessageHandler;
import pro.medguide.MedGuideTelegramBot.materials.UsersStates;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MedGuideBot extends SpringWebhookBot {

    String botPath, botUsername, botToken;

    MessageHandler messageHandler;
    CallbackQueryHandler callbackQueryHandler;

    public static HashMap<String, String> usersStates = new HashMap<>();

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
            e.printStackTrace();
            return new SendMessage(update.getMessage().getChatId().toString(),
                    "Exception handling TODO"); // TODO
        }
    }

    private BotApiMethod<?> handleUpdate(Update update) throws IOException, TelegramApiException {

        usersStates.putIfAbsent(update.getMessage().getChatId().toString(), UsersStates.STATIC.getStateTitle());

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

                String chatID = update.getMessage().getChatId().toString();

                if (usersStates.get(chatID).equals(UsersStates.SPECIALISTS.toString())) {

                    if (update.getMessage().getText().equals("↩️ Назад")) {
                        return messageHandler.handleMessage(update.getMessage());
                    }

                    SendPhoto sendPhoto = new SendPhoto();
                    sendPhoto.setParseMode(ParseMode.HTML);

                    switch (update.getMessage().getText()) {

                        case "Гастроэнтеролог" -> {
                                sendPhoto.setChatId(chatID);
                                sendPhoto.setCaption("<b>Гастроэнтеролог - Брагина Таисья Владимировна</b>\n" +
                                        "Подробнее <a href=\"https://medgid.pro/specialist/bragina-taisya-vladimirovna/\">по ссылке</a>");
                                sendPhoto.setPhoto(new InputFile(
                                        new File("src/main/java/pro/medguide/MedGuideTelegramBot/materials/images/bragina_t_v.jpg")));
                        }

                        case "Психолог" -> {
                                sendPhoto.setChatId(chatID);
                                sendPhoto.setCaption("<b>Психолог - Исаева Алла Михайловна</b>\n" +
                                        "Подробнее <a href=\"https://medgid.pro/specialist/isaeva-alla-mihajlovna/\">по ссылке</a>");
                                sendPhoto.setPhoto(new InputFile(
                                        new File("src/main/java/pro/medguide/MedGuideTelegramBot/materials/images/isaeva_a_m.jpg")));
                        }

                        case "Врач общей практики, терапевт" -> {
                            sendPhoto.setChatId(chatID);
                            sendPhoto.setCaption("<b>Врач общей практики - Морозов Андрей Юрьевич</b>\n" +
                                    "Подробнее <a href=\"https://medgid.pro/specialist/morozov-andrej-yurevich/\">по ссылке</a>");
                            sendPhoto.setPhoto(new InputFile(
                                    new File("src/main/java/pro/medguide/MedGuideTelegramBot/materials/images/morozov_a_u.jpg")));
                        }

                    }

                    execute(sendPhoto);
                    return null;

                }

                return messageHandler.handleMessage(update.getMessage());

            }

        }

        // PLUG
        return null;

    }

}
