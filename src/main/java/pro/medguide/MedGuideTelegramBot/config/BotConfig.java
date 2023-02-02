package pro.medguide.MedGuideTelegramBot.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class BotConfig {

    @Value("${telegram.bot-name}")
    String botName;

    @Value("${telegram.bot-token}")
    String token;

    @Value("${telegram.webhook-path}")
    String webhookPath;

}
