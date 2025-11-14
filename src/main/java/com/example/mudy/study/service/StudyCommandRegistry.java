package com.example.mudy.study.service;

import com.example.mudy.common.CommandRegistry;
import com.example.mudy.study.command.StudyCommand;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StudyCommandRegistry implements CommandRegistry {

    public List<CommandData> getCommands() {
        return Arrays.stream(StudyCommand.values())
                .map(StudyCommand::getCommandData)
                .collect(Collectors.toList());
    }
}
