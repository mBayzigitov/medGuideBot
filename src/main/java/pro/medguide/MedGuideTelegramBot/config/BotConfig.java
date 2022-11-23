package pro.medguide.MedGuideTelegramBot.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

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
