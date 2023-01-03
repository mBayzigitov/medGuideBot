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
            return new SendMessage(update.getMessage().getChatId().toString(),"❌ Возникла ошибка, попробуйте позже.");
        }
    }

    private BotApiMethod<?> handleUpdate(Update update) throws IOException, TelegramApiException {

        String chatID;

        try {
            chatID = update.getMessage().getChatId().toString();
        } catch (NullPointerException nullPointerException) {
            chatID = update.getCallbackQuery().getMessage().getChatId().toString();
        }

        usersStates.putIfAbsent(chatID, UsersStates.STATIC.getStateTitle());

        // Handling buttons pressing
        if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            SendPhoto addresses_message = new SendPhoto();
            addresses_message.setChatId(chatID);
            addresses_message.setParseMode(ParseMode.HTML);

            switch (data) {

                case "CHOSE_RU" -> {
                    addresses_message.setCaption("<b>Адреса приёма анализа ХМС по Осипову в России</b>\n");
                    addresses_message.setPhoto(new InputFile(
                            new File("src/main/java/pro/medguide/MedGuideTelegramBot/materials/images/ru_addresses.jpg")));
                }

                case "CHOSE_KZ" -> {
                    addresses_message.setCaption("<b>Адреса приёма анализа ХМС по Осипову в Казахстане</b>\n");
                    addresses_message.setPhoto(new InputFile(
                            new File("src/main/java/pro/medguide/MedGuideTelegramBot/materials/images/kz_addresses.png")));
                }

            }

            execute(addresses_message);
            return null;
        }

        // Handling commands or usual messages
        if (update.hasMessage()) {

            // if command
            if (update.getMessage().hasEntities()) {

                return messageHandler.handleCommand(update.getMessage());

            } else {

                if (update.getMessage().getText().equals("\uD83D\uDCB3 Информация об оплате")) {
                    SendPhoto paymentsInfoMessage = new SendPhoto();

                    paymentsInfoMessage.setChatId(chatID);
                    paymentsInfoMessage.setParseMode(ParseMode.HTML);
                    paymentsInfoMessage.setCaption("<b>Оплата</b>\n\n" +
                            "✅ После подтверждения доктора, мы отправим Вам ответ на <b>электронную почту</b>.\n" +
                            "✅ Отсканируйте <b>QR-код</b> банковским приложением\n" +
                            "✅ В назначении платежа укажите ФИО и наименование услуги. Например, Иванов.И.И. - анализ " +
                            "ХМС или другая услуга. В поле \"Сумма\" введите сумму заказа, которую вам рассчитал наш специалист.\n\n" +
                            "<b>Для оплаты откройте ваше банковское приложение, выберите \"Оплата по QR-коду\", наведите камеру телефона на QR код. " +
                            "Реквизиты для перевода автоматически подставятся в данные платежа.</b>");
                    paymentsInfoMessage.setPhoto(new InputFile(
                            new File("src/main/java/pro/medguide/MedGuideTelegramBot/materials/images/payment_qr.png")));

                    execute(paymentsInfoMessage);
                    return null;
                }

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

                        case "Терапевт" -> {
                            sendPhoto.setChatId(chatID);
                            sendPhoto.setCaption("<b>Терапевт - Пейкова Элина Викторовна</b>\n" +
                                    "Подробнее <a href=\"https://medgid.pro/specialist/pejkova-elina-viktorovna/\">по ссылке</a>");
                            sendPhoto.setPhoto(new InputFile(
                                    new File("src/main/java/pro/medguide/MedGuideTelegramBot/materials/images/peykova_e_v.jpg")));
                        }

                        case "Кардиолог" -> {
                            sendPhoto.setChatId(chatID);
                            sendPhoto.setCaption("<b>Кардиолог - Тараканов Александр Александрович</b>\n" +
                                    "Подробнее <a href=\"https://medgid.pro/specialist/tarakanov-aleksandr-aleksandrovich/\">по ссылке</a>");
                            sendPhoto.setPhoto(new InputFile(
                                    new File("src/main/java/pro/medguide/MedGuideTelegramBot/materials/images/tarakanov_a_a.jpg")));
                        }

                        case "Генетик" -> {
                            sendPhoto.setChatId(chatID);
                            sendPhoto.setCaption("<b>Генетик - Амирова Татьяна Олеговна</b>\n" +
                                    "Подробнее <a href=\"https://medgid.pro/specialist/amirova-tatyana-olegovna/\">по ссылке</a>");
                            sendPhoto.setPhoto(new InputFile(
                                    new File("src/main/java/pro/medguide/MedGuideTelegramBot/materials/images/amirova_t_o.jpg")));
                        }

                        case "Дерматовенеролог" -> {
                            sendPhoto.setChatId(chatID);
                            sendPhoto.setCaption("<b>Дерматовенеролог - Кущ Ирина Вячеславовна</b>\n" +
                                    "Подробнее <a href=\"https://medgid.pro/specialist/kushh-irina-vyacheslavovna/\">по ссылке</a>");
                            sendPhoto.setPhoto(new InputFile(
                                    new File("src/main/java/pro/medguide/MedGuideTelegramBot/materials/images/kush_i_v.jpg")));
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
