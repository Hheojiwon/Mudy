package com.example.mudy.study.command.assignment;

import com.example.mudy.study.command.StudyCommand;
import com.example.mudy.study.service.AssignmentService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class AssignmentDeleteCommand extends ListenerAdapter {

    private AssignmentService assignmentService;

    public AssignmentDeleteCommand(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
           if (!event.getName().equals(StudyCommand.DELETE.getName())) { return; }

           assignmentService.deleteAssignment(event.getUser().getId(), event.getOption("title").getAsString());

           event.reply("⛔️삭제를 완료하였습니다.").queue();
    }
}
