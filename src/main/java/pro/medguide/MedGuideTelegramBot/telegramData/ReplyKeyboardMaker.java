package pro.medguide.MedGuideTelegramBot.telegramData;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;
import pro.medguide.MedGuideTelegramBot.materials.ButtonsNamesEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Component
public class ReplyKeyboardMaker {

    public ReplyKeyboardMarkup getMainMenuKeyboard() {

        KeyboardRow row_1 = new KeyboardRow();
        row_1.add("ℹ️ " + ButtonsNamesEnum.CONTACT_US.getButtonName());
        row_1.add("\uD83D\uDC69\u200D⚕️ " + ButtonsNamesEnum.SPECIALISTS.getButtonName());

        KeyboardRow row_2 = new KeyboardRow();
        row_2.add("\uD83C\uDFE5 " + ButtonsNamesEnum.RECEPTION_ADRESSES.getButtonName());
        row_2.add("\uD83D\uDCC8 " + ButtonsNamesEnum.PARTNER_DISCOUNTS.getButtonName());

        KeyboardRow row_3 = new KeyboardRow();
        row_3.add("\uD83D\uDC8A " + ButtonsNamesEnum.SERVICES.getButtonName());

        KeyboardRow row_4 = new KeyboardRow();
        row_4.add("\uD83D\uDCB3 " + ButtonsNamesEnum.PAYMENT_INFO.getButtonName());

        KeyboardRow row_5 = new KeyboardRow();
        row_5.add("⬇️ " + ButtonsNamesEnum.HIDE_KEYBOARD.getButtonName());

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        keyboardRowList.add(row_1);
        keyboardRowList.add(row_2);
        keyboardRowList.add(row_3);
        keyboardRowList.add(row_4);
        keyboardRowList.add(row_5);

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        return replyKeyboardMarkup;

    }

    public ReplyKeyboardMarkup getSpecialistsKeyboard() {

        KeyboardRow row_1 = new KeyboardRow();
        row_1.add("Гастроэнтеролог");
        row_1.add("Психолог");

        KeyboardRow row_2 = new KeyboardRow();
        row_2.add("Врач общей практики, терапевт");
        row_2.add("Терапевт");

        KeyboardRow row_3 = new KeyboardRow();
        row_3.add("Кардиолог");
        row_3.add("Генетик");

        KeyboardRow row_4 = new KeyboardRow();
        row_4.add("Дерматовенеролог");

        KeyboardRow row_5 = new KeyboardRow();
        row_5.add("↩️ Назад");

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        keyboardRowList.add(row_1);
        keyboardRowList.add(row_2);
        keyboardRowList.add(row_3);
        keyboardRowList.add(row_4);
        keyboardRowList.add(row_5);

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        return replyKeyboardMarkup;

    }

}
