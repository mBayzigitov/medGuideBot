package pro.medguide.MedGuideTelegramBot.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import pro.medguide.MedGuideTelegramBot.handlers.CallbackQueryHandler;
import pro.medguide.MedGuideTelegramBot.handlers.MessageHandler;
import pro.medguide.MedGuideTelegramBot.service.MedGuideBot;

@Configuration
@AllArgsConstructor
public class SpringConfig {
    private final BotConfig telegramConfig;

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(telegramConfig.getWebhookPath()).build();
    }

    @Bean
    public MedGuideBot springWebhookBot(SetWebhook setWebhook,
                                        MessageHandler messageHandler,
                                        CallbackQueryHandler callbackQueryHandler) {
        MedGuideBot bot = new MedGuideBot(setWebhook, messageHandler, callbackQueryHandler);

        bot.setBotPath(telegramConfig.getWebhookPath());
        bot.setBotUsername(telegramConfig.getBotName());
        bot.setBotToken(telegramConfig.getToken());

        return bot;
    }
}