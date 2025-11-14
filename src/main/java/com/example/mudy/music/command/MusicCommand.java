package com.example.mudy.music.command;

import lombok.Getter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

@Getter
public enum MusicCommand {

    PLAY("재생", "공부하기 좋은 조용한 음악을 재생합니다."),

    STOP("정지", "음악 재생을 멈추고 큐를 비웁니다."),

    PAUSE("일시정지", "음악을 일시 정지 합니다."),

    SKIP("다음곡", "현재 곡을 건너뛰고 다음 곡을 재생합니다.");


    private final CommandData commandData;

    MusicCommand(String name, String description) {
        this.commandData = Commands.slash(name, description);
    }

    MusicCommand(String name, String description, OptionData... options) {
        this.commandData = Commands.slash(name, description).addOptions(options);
    }

    public String getName() {
        return this.commandData.getName();
    }
}