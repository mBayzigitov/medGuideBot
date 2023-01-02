package pro.medguide.MedGuideTelegramBot.handlers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import pro.medguide.MedGuideTelegramBot.materials.UsersStates;
import pro.medguide.MedGuideTelegramBot.telegramData.ReplyKeyboardMaker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

import static pro.medguide.MedGuideTelegramBot.service.MedGuideBot.usersStates;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class MessageHandler {

    static ReplyKeyboardMaker replyKeyboardMaker = new ReplyKeyboardMaker();

    public BotApiMethod<?> handleMessage(Message message) throws FileNotFoundException, TelegramApiException {

        String chatID = message.getChatId().toString();
        String inputText = message.getText();

        if (inputText == null) {
            throw new IllegalArgumentException();
        }

        switch (inputText) {

            case "⬇️ Скрыть" -> {
                SendMessage sendMessage = new SendMessage(chatID,
                        "Панель скрыта. Для отображения меню воспользуйтесь командой /showkeyboard");

                sendMessage.enableMarkdown(true);
                sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));

                return sendMessage;
            }

            case "ℹ️ Контактная информация" -> {

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

                usersStates.put(chatID, String.valueOf(UsersStates.SPECIALISTS));

                SendMessage sendMessage = new SendMessage();

                sendMessage.setChatId(chatID);
                sendMessage.setText("\uD83D\uDC68\u200D⚕️ <b>Выберите интересующего вас специалиста:</b>");
                sendMessage.setParseMode(ParseMode.HTML);
                sendMessage.setReplyMarkup(replyKeyboardMaker.getSpecialistsKeyboard());

                return sendMessage;

            }

            case "↩️ Назад" -> {
                usersStates.put(chatID, String.valueOf(UsersStates.STATIC));

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatID);
                sendMessage.setText("↩️ Назад");
                sendMessage.setReplyMarkup(replyKeyboardMaker.getMainMenuKeyboard());
                return sendMessage;
            }

        }

        // PLUG
        return null;

    }

    public SendMessage getStartMessage(String chatID) {
        SendMessage sendMessage = new SendMessage(chatID, "Здравствуйте! Воспользуйтесь клавиатурой выбора услуги");
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(replyKeyboardMaker.getMainMenuKeyboard());
        return sendMessage;
    }

    public BotApiMethod<?> handleCommand(Message message) {

        Optional<MessageEntity> commandEntity =
                message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();

        String chatID = message.getChatId().toString();

        if (commandEntity.isPresent()) {

            String command = message.getText().substring(commandEntity.get().getOffset(), commandEntity.get().getLength());

            switch (command) {

                case "/start" -> {
                    return getStartMessage(chatID);
                }

                case "/showkeyboard" -> {
                    SendMessage sendMessage = new SendMessage(chatID, "Главное меню включено");
                    sendMessage.enableMarkdown(true);
                    sendMessage.setReplyMarkup(replyKeyboardMaker.getMainMenuKeyboard());
                    return sendMessage;
                }

            }

        }


        // PLUG
        return null;

    }

}
