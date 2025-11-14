package com.example.mudy.study.command;

import lombok.Getter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

@Getter
public enum StudyCommand {
    REGISTER("과제등록", "스터디 과제를 등록합니다.",
            new OptionData(OptionType.STRING, "title", "과제명", true),
            new OptionData(OptionType.STRING, "deadline", "마감일 (yyyy-MM-dd HH:mm)", true)),
    LIST("과제목록", "스터디 과제 목록을 조회합니다."),
    COMPLETE("과제완료", "과제를 완료처리합니다.",
            new OptionData(OptionType.STRING, "title", "과제명", true)),
    DELETE("과제삭제", "등록한 과제를 삭제합니다.",
            new OptionData(OptionType.STRING, "title", "과제명", true)),
    COMPLETE_RATE("과제완료율", "유저의 과제 완료율을 확인합니다.");

    private final CommandData commandData;

    StudyCommand(String title, String description) {
        this.commandData = Commands.slash(title, description);
    }

    StudyCommand(String title, String description, OptionData... optionData) {
        this.commandData = Commands.slash(title, description).addOptions(optionData);
    }

    public String getName() {
        return commandData.getName();
    }
}
