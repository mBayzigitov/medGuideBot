package pro.medguide.MedGuideTelegramBot.handlers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import pro.medguide.MedGuideTelegramBot.telegramData.ReplyKeyboardMaker;

import java.util.Optional;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class MessageHandler {

    ReplyKeyboardMaker replyKeyboardMaker;

    public BotApiMethod<?> handleMessage(Message message) {

        String chatID = message.getChatId().toString();
        String inputText = message.getText();

        if (inputText == null) {
            throw new IllegalArgumentException();
        }

        switch (inputText) {

            case "Скрыть" -> {
                SendMessage sendMessage = new SendMessage(chatID, "Панель скрыта");
                sendMessage.enableMarkdown(true);
                sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));
                return sendMessage;
            }

        }

        // PLUG
        return null;

    }

    public SendMessage getStartMessage(String chatID) {
        SendMessage sendMessage = new SendMessage(chatID, "Здравствуйте! Воспользуйтесь клавиатурой для ввода команды");
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

            }

        }


        // PLUG
        return null;

    }

}
