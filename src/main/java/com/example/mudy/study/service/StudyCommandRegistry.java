package com.example.mudy.study.service;

import com.example.mudy.common.CommandRegistry;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StudyCommandRegistry implements CommandRegistry {

    public List<CommandData> getCommands() {
        return List.of(
                Commands.slash("과제등록", "스터디 과제를 등록합니다.")
                        .addOption(OptionType.STRING, "title", "과제명", true)
                        .addOption(OptionType.STRING, "deadline", "마감일 (yyyy-MM-dd HH:mm)", true)
        );
    }
}
