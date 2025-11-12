package com.example.mudy.music.command;

import com.example.mudy.common.CommandRegistry;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MusicCommandRegistry implements CommandRegistry {

    @Override
    public List<CommandData> getCommands() {
        return Arrays.stream(MusicCommand.values())
                .map(MusicCommand::getCommandData)
                .collect(Collectors.toList());
    }
}
