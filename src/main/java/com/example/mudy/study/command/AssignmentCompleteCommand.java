package com.example.mudy.study.command;

import com.example.mudy.study.repository.AssignmentRepository;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class AssignmentCompleteCommand extends ListenerAdapter {

    AssignmentRepository assignmentRepository;

    public AssignmentCompleteCommand(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals("과제완료")) { return; }

        assignmentRepository.complete(event.getUser().getId(), event.getOption("title").getAsString());

        event.reply("✅ 과제를 완료하였습니다.").queue();
    }
}
