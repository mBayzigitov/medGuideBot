package pro.medguide.MedGuideTelegramBot.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("application.properties")
public class BotConfig {

    @Value("${telegram.bot-name}")
    String botName;

    @Value("${telegram.bot-token}")
    String token;

    @Value("${telegram.webhook-path}")
    String webhookPath;

}
