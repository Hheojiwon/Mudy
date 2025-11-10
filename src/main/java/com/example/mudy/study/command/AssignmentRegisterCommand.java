package com.example.mudy.study.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class AssignmentRegisterCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("과제등록")) return;

        String title = event.getOption("title").getAsString();
        String deadline = event.getOption("deadline").getAsString();
        LocalDateTime deadlineTime = LocalDateTime.parse(deadline, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        Duration remainTime = Duration.between(LocalDateTime.now(), deadlineTime);
        long days = remainTime.toDays();
        long hours = remainTime.minusDays(days).toHours();
        long minutes = remainTime.minusDays(days).minusHours(hours).toMinutes();
        String remainTimeText = String.format("%d일 %d시간 %d분 남음", days, hours, minutes);

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("✅ 과제 등록 완료")
                .setColor(0x57F287)
                .addField("📝 과제명", title, false)
                .addField("⌛ 남은 시간", remainTimeText, false);
        event.replyEmbeds(embed.build()).queue();
    }
}
