package pro.medguide.MedGuideTelegramBot.materials;

public enum UsersStates {

    STATIC("STATIC"),
    SPECIALISTS("SPECIALISTS");

    private String stateTitle;

    UsersStates(String stringValue) {
        this.stateTitle = stringValue;
    }

    public String getStateTitle() {
        return stateTitle;
    }

}
