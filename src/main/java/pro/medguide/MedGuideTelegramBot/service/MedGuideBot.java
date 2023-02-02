package pro.medguide.MedGuideTelegramBot.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;
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
    public static Logger logger = LoggerFactory.getLogger(MedGuideBot.class);

    public static int usersCounter;
    public static int buttonsClickCounter;
    public static int counterOfOrdersRequests;

    public static HashMap<String, String> usersStates = new HashMap<>();

    public MedGuideBot(SetWebhook setWebhook, MessageHandler messageHandler) {
        super(setWebhook);
        this.messageHandler = messageHandler;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
            return handleUpdate(update);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new SendMessage(update.getMessage().getChatId().toString(),"❌ Возникла ошибка, попробуйте позже.");
        }
    }

    private BotApiMethod<?> handleUpdate(Update update) throws IOException, TelegramApiException {

        String chatID;

        // trying to get chat id from message, if it's callback, then taking it from callback query
        try {
            chatID = update.getMessage().getChatId().toString();
        } catch (NullPointerException nullPointerException) {
            chatID = update.getCallbackQuery().getMessage().getChatId().toString();
        }

        if (!usersStates.containsKey(chatID)) {
            usersCounter++;
            logger.info("New user has connected. Current number of users: " + usersCounter);
        }

        // if update is from new user, then put static state in the hashmap for him
        usersStates.putIfAbsent(chatID, UsersStates.STATIC.getStateTitle());

        // handling of inline buttons pressing
        if (update.hasCallbackQuery()) {
            buttonsClickCounter++;
            logger.info("User with ID:[" + chatID + "] has clicked on \"Адреса приёма\" button");
            String data = update.getCallbackQuery().getData();
            SendPhoto addresses_message = new SendPhoto();
            addresses_message.setChatId(chatID);
            addresses_message.setParseMode(ParseMode.HTML);

            switch (data) {

                case "CHOSE_RU" -> {
                    addresses_message.setCaption("<b>Адреса приёма анализа ХМС по Осипову в России</b>\n\n" +
                            "Посмотреть <a href=\"https://medgid.pro/adresa-priyoma-analiza-hms-po-osipovu/\">на сайте</a>");
                    addresses_message.setPhoto(new InputFile(
                            new File("src/main/java/pro/medguide/MedGuideTelegramBot/materials/images/ru_addresses.jpg")));
                }

                case "CHOSE_KZ" -> {
                    addresses_message.setCaption("<b>Адреса приёма анализа ХМС по Осипову в России</b>\n\n" +
                            "Посмотреть <a href=\"https://medgid.pro/adresa-priyoma-analiza-hms-po-osipovu/\">на сайте</a>");
                    addresses_message.setPhoto(new InputFile(
                            new File("src/main/java/pro/medguide/MedGuideTelegramBot/materials/images/kz_addresses.png")));
                }

            }

            execute(addresses_message);

            // returns nothing, because message was sent via execute function
            return null;
        }

        // handling commands or usual messages
        if (update.hasMessage()) {

            // if command
            if (update.getMessage().hasEntities()) {

                return messageHandler.handleCommand(update.getMessage());

            } else {

                if (update.getMessage().getText().equals("\uD83D\uDCB3 Информация об оплате")) {

                    buttonsClickCounter++;
                    logger.info("User with ID:[" + chatID + "] has clicked on \"Информация об оплате\" button");

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

                    // returns nothing, because message was sent via execute function
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
                            sendPhoto.setCaption("<b>Гастроэнтеролог - Морозов Андрей Юрьевич</b>\n" +
                                    "Подробнее <a href=\"https://medgid.pro/specialist/morozov-andrej-yurevich/\">по ссылке</a>");
                            sendPhoto.setPhoto(new InputFile(
                                    new File("src/main/java/pro/medguide/MedGuideTelegramBot/materials/images/morozov_a_u.jpg")));
                        }

                        case "Психолог", "Хэлс коуч", "Нутрициолог" -> {
                                sendPhoto.setChatId(chatID);
                                sendPhoto.setCaption("<b>Психолог, хэлс-коуч, нутрициолог - Исаева Алла Михайловна</b>\n" +
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
                            sendPhoto.setCaption("<b>Терапевт - Брагина Таисья Владимировна</b>\n" +
                                    "Подробнее <a href=\"https://medgid.pro/specialist/bragina-taisya-vladimirovna/\">по ссылке</a>");
                            sendPhoto.setPhoto(new InputFile(
                                    new File("src/main/java/pro/medguide/MedGuideTelegramBot/materials/images/bragina_t_v.jpg")));
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

                        case "Дерматовенеролог, косметолог" -> {
                            sendPhoto.setChatId(chatID);
                            sendPhoto.setCaption("<b>Дерматовенеролог, косметолог - Пейкова Элина Викторовна</b>\n" +
                                    "Подробнее <a href=\"https://medgid.pro/specialist/pejkova-elina-viktorovna/\">по ссылке</a>");
                            sendPhoto.setPhoto(new InputFile(
                                    new File("src/main/java/pro/medguide/MedGuideTelegramBot/materials/images/peykova_e_v.jpg")));
                        }

                        default -> {
                            SendMessage wrongSpecialistMessage = new SendMessage();

                            wrongSpecialistMessage.setChatId(chatID);
                            wrongSpecialistMessage.setText("\uD83E\uDD37\uD83C\uDFFB\u200D♀️ Такого специалиста у нас нет.\n\n" +
                                    "Введите наименование специальности из списка или вернитесь в главное меню командой /start");
                            return wrongSpecialistMessage;
                        }


                    }

                    execute(sendPhoto);

                    // returns nothing, because message was sent via execute function
                    return null;

                }

                return messageHandler.handleMessage(update.getMessage());

            }

        }

        return null;

    }

}
