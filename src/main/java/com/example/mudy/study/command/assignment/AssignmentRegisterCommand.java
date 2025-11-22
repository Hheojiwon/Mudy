package com.example.mudy.study.command.assignment;

import com.example.mudy.study.command.StudyCommand;
import com.example.mudy.study.model.Assignment;
import com.example.mudy.study.service.AssignmentService;
import com.example.mudy.study.util.TimeTextGenerator;
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

    private final AssignmentService assignmentService;

    public AssignmentRegisterCommand(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals(StudyCommand.REGISTER.getName())) return;

        String title = event.getOption("title").getAsString();
        String deadline = event.getOption("deadline").getAsString();
        LocalDateTime deadlineTime = LocalDateTime.parse(deadline, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        Assignment assignment = assignmentService.registerAssignment(event.getUser().getId(), title, deadlineTime, event.getUser().getGlobalName(), event.getChannel().asTextChannel());

        String remainTimeText = TimeTextGenerator.generate(Duration.between(LocalDateTime.now(), assignment.getDeadline()));

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("✅ 과제 등록 완료")
                .setColor(0x57F287)
                .addField("📝 과제명", assignment.getTitle(), false)
                .addField("⌛ 남은 시간", remainTimeText, false);
        event.replyEmbeds(embed.build()).queue();
    }
}
