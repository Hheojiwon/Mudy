package com.example.mudy.global.config;

import com.example.mudy.common.CommandRegistry;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class BotConfig {

    @Value("${discord.token}")
    private String token;

    private final List<ListenerAdapter> listeners;
    private final List<CommandRegistry> commandRegistries;

    // 생성자 주입
    public BotConfig(List<ListenerAdapter> listeners, List<CommandRegistry> commandRegistries) {
        this.listeners = listeners;
        this.commandRegistries = commandRegistries;
    }

    @Bean
    public JDA jda() throws Exception {

        JDA jda = JDABuilder.createDefault(token)
                .enableIntents(
                        GatewayIntent.MESSAGE_CONTENT,
                        GatewayIntent.GUILD_VOICE_STATES  // 음성 채널용
                )
                .addEventListeners(listeners.toArray())
                .build()
                .awaitReady();

        jda.updateCommands()
                .addCommands(commandRegistries.stream()
                        .flatMap(registry -> registry.getCommands().stream())
                        .collect(Collectors.toList()))
                .queue();

        return jda;
    }
}
