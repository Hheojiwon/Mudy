package com.example.mudy.global;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

/*
봇 연결 확인용 클래스
 */

@Component
public class PingPongListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        String message = event.getMessage().getContentRaw();
        if (message.equals("!ping")) {
            event.getChannel().sendMessage("pong!").queue();
        }
    }
}