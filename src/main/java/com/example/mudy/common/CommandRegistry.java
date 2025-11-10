package com.example.mudy.common;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.List;

public interface CommandRegistry {
    public List<CommandData> getCommands();
}
