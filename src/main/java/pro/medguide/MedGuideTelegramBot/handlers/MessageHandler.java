package pro.medguide.MedGuideTelegramBot.handlers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import pro.medguide.MedGuideTelegramBot.MedGuideTelegramBotApplication;
import pro.medguide.MedGuideTelegramBot.materials.UsersStates;
import pro.medguide.MedGuideTelegramBot.service.MedGuideBot;
import pro.medguide.MedGuideTelegramBot.telegramData.KeyboardCreator;

import java.io.File;
import java.util.Optional;

import static pro.medguide.MedGuideTelegramBot.service.MedGuideBot.buttonsClickCounter;
import static pro.medguide.MedGuideTelegramBot.service.MedGuideBot.usersStates;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class MessageHandler {

    static KeyboardCreator keyboardCreator = new KeyboardCreator();

    public BotApiMethod<?> handleMessage(Message message) {

        String chatID = message.getChatId().toString();
        String inputText = message.getText();

        if (inputText == null) {
            throw new IllegalArgumentException();
        }

        if (chatID.equals("2024480073") && inputText.equals("info")) {
            return new SendMessage("2024480073",
                    "\uD83D\uDCC8 Статистика\n\n" +
                            "Всего пользователей: " + MedGuideBot.usersCounter + "\n" +
                            "Запросов на заказ: " + MedGuideBot.counterOfOrdersRequests + "\n" +
                            "Нажатий на кнопки: " + buttonsClickCounter);
        }

        // handling usual messages and queries from main keyboard
        switch (inputText) {

            case "⬇️ Скрыть" -> {
                MedGuideBot.buttonsClickCounter++;
                MedGuideBot.logger.info("User with ID:[" + chatID + "] has clicked on \"Скрыть\" button.\n" +
                        "Current number of buttons clicks: " + buttonsClickCounter);
                return hideKeyboard(chatID);
            }

            case "ℹ️ Контактная информация" -> {

                MedGuideBot.buttonsClickCounter++;
                MedGuideBot.logger.info("User with ID:[" + chatID + "] has clicked on \"Контактная информация\" button.\n" +
                        "Current number of buttons clicks: " + buttonsClickCounter);

                return new SendMessage(chatID, "\uD83D\uDCCD Адрес\n" +
                        "\tМосква, ул. А. Солженицына 27 офис 425\n\n" +
                        "\uD83D\uDCDE Контакты\n" +
                        "\tТелефон: +74957848705\n" +
                        "\tinfo@medgid.pro\n" +
                        "\tWhatsapp: +79857848705\n" +
                        "\tViber: +79857848705\n\n" +
                        "\uD83D\uDD54 Время работы\n" +
                        "\tПн-Пт: 10.00 - 16.00\n" +
                        "\tСб, Вс: Выходной\n\n" +
                        "\uD83E\uDD1D По вопросам сотрудничества\n" +
                        "\tТелефон: +74957848705\n" +
                        "\tПочта: 79857848705@yandex.ru");
            }

            case "\uD83D\uDC69\u200D⚕️ Специалисты" -> {

                MedGuideBot.buttonsClickCounter++;
                MedGuideBot.logger.info("User with ID:[" + chatID + "] has clicked on \"Специалисты\" button.\n" +
                        "Current number of buttons clicks: " + buttonsClickCounter);

                usersStates.put(chatID, String.valueOf(UsersStates.SPECIALISTS));

                SendMessage sendMessage = new SendMessage();

                sendMessage.setChatId(chatID);
                sendMessage.setText("\uD83D\uDC68\u200D⚕️ <b>Выберите интересующего вас специалиста:</b>");
                sendMessage.setParseMode(ParseMode.HTML);
                sendMessage.setReplyMarkup(keyboardCreator.getSpecialistsKeyboard());

                return sendMessage;

            }

            case "↩️ Назад" -> {

                MedGuideBot.buttonsClickCounter++;
                MedGuideBot.logger.info("User with ID:[" + chatID + "] has clicked on \"Назад\" button.\n" +
                        "Current number of buttons clicks: " + buttonsClickCounter);

                usersStates.put(chatID, String.valueOf(UsersStates.STATIC));

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatID);
                sendMessage.setText("↩️ Назад");
                sendMessage.setReplyMarkup(keyboardCreator.getMainMenuKeyboard());
                return sendMessage;
            }

            case "\uD83C\uDFE5 Адреса приёма" -> {
                MedGuideBot.buttonsClickCounter++;
                MedGuideBot.logger.info("User with ID:[" + chatID + "] has clicked on \"Адреса приёма\" button.\n" +
                        "Current number of buttons clicks: " + buttonsClickCounter);

                SendMessage sendMessage = new SendMessage();

                sendMessage.setChatId(chatID);
                sendMessage.setParseMode(ParseMode.HTML);
                sendMessage.setText("<b>Выберите интересующую вас страну:</b>");
                sendMessage.setReplyMarkup(keyboardCreator.getCountriesNamesButton());

                return sendMessage;
            }

            case "\uD83D\uDCC8 Скидки от партнёра" -> {

                MedGuideBot.buttonsClickCounter++;
                MedGuideBot.logger.info("User with ID:[" + chatID + "] has clicked on \"Скидки от партнёра\" button.\n" +
                        "Current number of buttons clicks: " + buttonsClickCounter);

                SendMessage discountsMessage = new SendMessage();

                discountsMessage.setChatId(chatID);
                discountsMessage.setParseMode(ParseMode.HTML);
                discountsMessage.setText("\uD83D\uDD39 <b>БакЗдрав</b> - производитель пробиотиков\n" +
                        "<a href=\"https://bakzdrav.ru/internet-magazin.html?refcode=medgid.pro&refsource=a9t81bho\">Получить скидку</a>\n\n" +
                        "\uD83D\uDD39 <b>Daigo – японская биологически активная добавка к пище.</b>\n" +
                        "БАД к пище «Дайго» является дополнительным источником витамина К2\n" +
                        "<a href=\"https://daigo.ru/?utm_source=vd&utm_medium=cpc&utm_campaign=test&utm_content=link&utm_term=medgidpro\">Перейти на сайт</a>\n\n" +
                        "\uD83D\uDD39 <b>МаПа</b> — Клиника превентивной медицины в Санкт-Петербурге.\n" +
                        "мы с радостью подарим Вам приветственные 700 Б на первую покупку по промокоду МедГид.pro. 1 Б = 1 рубль. Подробности программы лояльности доступны после регистрации.\n" +
                        "<a href=\"https://mapaclinic.uds.app/c/join?ref=medpro200\">Получить скидку</a>\n\n" +
                        "\uD83D\uDD39 Готовая еда для беременных и кормящих матерей с доставкой от <b>Накормим маму</b>\n" +
                        "Заказать можно на <a href=\"www.nakormimmamu.ru\">сайте</a> или по WhatsApp 89256109847, при использовании " +
                        "промокода «МЕДГИД.ПРО» получаете 5% скидку на заказ\n\n" +
                        "\uD83D\uDD39 <b>Эстетик поинт</b> — интернет-магазин косметики и средств по уходу за лицом, телом и волосами, а также b2b портал " +
                        "для косметологов, салонов красоты и компаний-дистрибьюторов.\n" +
                        "<a href=\"https://medgid.pro/rekomenduem/#ESTHETICS-POINT\">Получить скидку</a>");

                return discountsMessage;
            }

            case "\uD83D\uDCB3 Информация об оплате" -> {

                MedGuideBot.buttonsClickCounter++;
                MedGuideBot.logger.info("User with ID:[" + chatID + "] has clicked on \"Информация об оплате\" button.\n" +
                        "Current number of buttons clicks: " + buttonsClickCounter);

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
            }

            case "\uD83D\uDC8A Услуги" -> {

                MedGuideBot.buttonsClickCounter++;
                MedGuideBot.logger.info("User with ID:[" + chatID + "] has clicked on \"Информация об оплате\" button.\n" +
                        "Current number of buttons clicks: " + buttonsClickCounter);

                SendMessage servicesMessage = new SendMessage();

                servicesMessage.setChatId(chatID);
                servicesMessage.setParseMode(ParseMode.HTML);
                servicesMessage.setText("<b>Выберите услуги в панели:</b>");
                servicesMessage.setReplyMarkup(keyboardCreator.getServicesKeyboard());

                return servicesMessage;
            }

            case "✅ Заказать набор \"Сухая капля\"" -> {

                MedGuideBot.counterOfOrdersRequests++;
                MedGuideBot.logger.info("User with ID:[" + chatID + "] wants to order \"Набор Сухая Капля\".\n" +
                        "Current number of orders requests: " + MedGuideBot.counterOfOrdersRequests);

                SendMessage serviceMessage = new SendMessage();

                serviceMessage.setChatId(chatID);
                serviceMessage.setParseMode(ParseMode.HTML);
                serviceMessage.setText("✅ Услуга - заказ набора \"Сухая капля\"\n\n" +
                        "Перейдите по <a href=\"https://medgid.pro/uslugi/sdat-analiz-zakazat-nabor-suhaya-" +
                        "kaplya-ghms-po-osipovu-mikrobnye-markery/\">ссылке</a> и следуйте инструкциям\n\n" +
                        "<b>Информацию об оплате</b> можно посмотреть в главном меню или на сайте");

                return serviceMessage;
            }

            case "✅ Заказать \"расшифровку анализа ХМС и/или других клинических анализов\"" -> {

                MedGuideBot.counterOfOrdersRequests++;
                MedGuideBot.logger.info("User with ID:[" + chatID + "] wants to order \"Расшифровка анализа ХМС и/или других клинических анализов\".\n" +
                        "Current number of orders requests: " + MedGuideBot.counterOfOrdersRequests);

                SendMessage serviceMessage = new SendMessage();

                serviceMessage.setChatId(chatID);
                serviceMessage.setParseMode(ParseMode.HTML);
                serviceMessage.setText("✅ Услуга - заказ расшифровки анализов\n\n" +
                        "Перейдите по <a href=\"https://medgid.pro/uslugi/poluchit-rasshifrovku-analiza-" +
                        "diagnostiki-interpretacziyu/\">ссылке</a> и следуйте инструкциям\n\n" +
                        "<b>Информацию об оплате</b> можно посмотреть в главном меню или на сайте");

                return serviceMessage;
            }

            case "✅ Получить индивидуально схему приема пробиотиков на основе анализа" -> {

                MedGuideBot.counterOfOrdersRequests++;
                MedGuideBot.logger.info("User with ID:[" + chatID + "] wants to order \"Индивидуальная схема приема пробиотиков\".\n" +
                        "Current number of orders requests: " + MedGuideBot.counterOfOrdersRequests);

                SendMessage serviceMessage = new SendMessage();

                serviceMessage.setChatId(chatID);
                serviceMessage.setParseMode(ParseMode.HTML);
                serviceMessage.setText("✅ Услуга - индивидуальная схема пробиотиков от \"БакЗдрав\"\n\n" +
                        "Перейдите по <a href=\"https://medgid.pro/uslugi/podbor-probiotika-s-" +
                        "uchetom-vashego-analiza-hms-po-osipovu/\">ссылке</a> и следуйте инструкциям\n\n" +
                        "<b>Информацию об оплате</b> можно посмотреть в главном меню или на сайте");

                return serviceMessage;
            }

            case "✅ Записаться на онлайн-консультацию к специалисту" -> {

                MedGuideBot.counterOfOrdersRequests++;
                MedGuideBot.logger.info("User with ID:[" + chatID + "] wants to order \"Онлайн-консультация у специалиста\".\n" +
                        "Current number of orders requests: " + MedGuideBot.counterOfOrdersRequests);

                SendMessage serviceMessage = new SendMessage();

                serviceMessage.setChatId(chatID);
                serviceMessage.setParseMode(ParseMode.HTML);
                serviceMessage.setText("✅ Услуга - онлайн-консультация со специалистом\n\n" +
                        "Перейдите по <a href=\"https://medgid.pro/uslugi/zapisatsya-na-" +
                        "konsultacziyu-k-doktoru-udalyonno/\">ссылке</a> и следуйте инструкциям\n\n" +
                        "<b>Информацию об оплате</b> можно посмотреть в главном меню или на сайте");

                return serviceMessage;
            }

        }

        return new SendMessage(chatID, "\uD83E\uDD37\uD83C\uDFFB\u200D♀️ На данный момент ни одна команда не используется.");

    }

    public BotApiMethod<?> handleCommand(Message message) {

        Optional<MessageEntity> commandEntity =
                message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();

        String chatID = message.getChatId().toString();

        if (commandEntity.isPresent()) {

            String command = message.getText().substring(commandEntity.get().getOffset(), commandEntity.get().getLength());

            switch (command) {

                case "/start" -> {
                    usersStates.put(chatID, UsersStates.STATIC.getStateTitle());
                    SendMessage sendMessage = new SendMessage(chatID, "Здравствуйте! Воспользуйтесь клавиатурой выбора услуги");
                    sendMessage.enableMarkdown(true);
                    sendMessage.setReplyMarkup(keyboardCreator.getMainMenuKeyboard());
                    return sendMessage;
                }

                case "/showkeyboard" -> {
                    usersStates.put(chatID, UsersStates.STATIC.getStateTitle());
                    SendMessage sendMessage = new SendMessage(chatID, "Главное меню включено");
                    sendMessage.enableMarkdown(true);
                    sendMessage.setReplyMarkup(keyboardCreator.getMainMenuKeyboard());
                    return sendMessage;
                }

                case "/hidekeyboard" -> {
                    return hideKeyboard(chatID);
                }

            }

        }

        return new SendMessage(chatID, "\uD83E\uDD37\uD83C\uDFFB\u200D♀️ Введенная команда никак не используется. Пожалуйста, воспользуйтесь списком команд в меню.");

    }

    private BotApiMethod<?> hideKeyboard(String chatID) {
        usersStates.put(chatID, UsersStates.STATIC.getStateTitle());

        SendMessage sendMessage = new SendMessage(chatID,
                "Панель скрыта. Для отображения меню воспользуйтесь командой /showkeyboard");

        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));

        return sendMessage;
    }

}
