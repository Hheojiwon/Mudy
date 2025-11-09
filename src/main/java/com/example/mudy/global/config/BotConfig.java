package com.example.mudy.global.config;

import com.example.mudy.global.PingPongListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
public class BotConfig {

    @Value("${discord.token}")
    private String token;

    private final PingPongListener pingPongListener;

    // 생성자 주입
    public BotConfig(PingPongListener pingPongListener) {
        this.pingPongListener = pingPongListener;
    }

    @Bean
    public JDA jda() throws Exception {

        return JDABuilder.createDefault(token)
                .enableIntents(
                        GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_VOICE_STATES  // 음성 채널용
                )
                 .addEventListeners(pingPongListener)
                .build()
                .awaitReady();
    }
}
