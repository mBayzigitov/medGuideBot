package pro.medguide.MedGuideTelegramBot.telegramData;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import pro.medguide.MedGuideTelegramBot.materials.ButtonsNamesEnum;

import java.util.ArrayList;
import java.util.List;

@Component
public class ReplyKeyboardMaker {

    public ReplyKeyboardMarkup getMainMenuKeyboard() {

        KeyboardRow row_1 = new KeyboardRow();
        row_1.add(ButtonsNamesEnum.CONTACT_US.getButtonName());
        row_1.add(ButtonsNamesEnum.SPECIALISTS.getButtonName());

        KeyboardRow row_2 = new KeyboardRow();
        row_2.add(ButtonsNamesEnum.RECEPTION_ADRESSES.getButtonName());
        row_2.add(ButtonsNamesEnum.PARTNER_DISCOUNTS.getButtonName());

        KeyboardRow row_3 = new KeyboardRow();
        row_3.add(ButtonsNamesEnum.SERVICES.getButtonName());

        KeyboardRow row_4 = new KeyboardRow();
        row_4.add(ButtonsNamesEnum.PAYMENT_INFO.getButtonName());

        KeyboardRow row_5 = new KeyboardRow();
        row_5.add(ButtonsNamesEnum.HIDE_KEYBOARD.getButtonName());

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
