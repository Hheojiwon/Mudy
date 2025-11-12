package com.example.mudy.study.command;

import com.example.mudy.study.model.Assignment;
import com.example.mudy.study.service.AssignmentService;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AssignmentListCommand extends ListenerAdapter {

    private final AssignmentService assignmentService;

    public AssignmentListCommand(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @Override
    public void onSlashCommandInteraction (@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("과제목록")) return;

        List<Assignment> userAssignments =  assignmentService.getUserAssignments(event.getUser().getId());

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("📋 과제 목록")
                .setColor(0x57F287);
        for (Assignment assignment : userAssignments) {
            String state = " : 🔄진행중";
            if (assignment.isCompleted()) { state = " : ✅완료"; }
            embed.addField(assignment.getTitle() + state, "- " + assignment.getDeadline().toString() + "까지", false);
        }

        event.replyEmbeds(embed.build()).queue();
    }
}
