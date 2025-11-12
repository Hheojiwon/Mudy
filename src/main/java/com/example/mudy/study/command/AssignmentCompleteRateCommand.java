package com.example.mudy.study.command;

import com.example.mudy.study.model.Assignment;
import com.example.mudy.study.service.AssignmentService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AssignmentCompleteRateCommand extends ListenerAdapter {

    private AssignmentService assignmentService;

    public AssignmentCompleteRateCommand(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @Override
    public void onSlashCommandInteraction (@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("과제완료율")) return;

        List<Assignment> userAssignments =  assignmentService.getUserAssignments(event.getUser().getId());

        long completeCount = userAssignments.stream()
                .filter(Assignment::isCompleted)
                .count();

        long assignmentCompleteRate = completeCount * 100 / userAssignments.size();

        event.reply(event.getUser().getGlobalName() + "님의 과제 완료율은 " + assignmentCompleteRate + "%입니다.").queue();
    }
}
