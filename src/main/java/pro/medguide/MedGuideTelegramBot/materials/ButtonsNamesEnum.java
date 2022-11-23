package pro.medguide.MedGuideTelegramBot.materials;

public enum ButtonsNamesEnum {

    CONTACT_US("Контактная информация"),
    HIDE_KEYBOARD("Скрыть"),
    SPECIALISTS("Специалисты"),
    RECEPTION_ADRESSES("Адреса приёма"),
    PARTNER_DISCOUNTS("Скидки от партнёра"),
    SERVICES("Услуги"),
    PAYMENT_INFO("Информация об оплате"),
    ORDER_SET("Заказать набор"),
    MAKE_APPOINTMENT("Записаться на прием"),
    GET_PERSONAL_DIET("Получить персональную диету"),
    GET_CONSULTATION("Онлайн консультация...");

    private final String buttonName;

    ButtonsNamesEnum(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonName() {
        return buttonName;
    }

}
