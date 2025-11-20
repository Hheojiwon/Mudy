package com.example.mudy.music.command;

import lombok.Getter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

@Getter
public enum MusicCommand {

    PLAY("재생", "공부하기 좋은 조용한 음악을 재생합니다."),

    STOP("정지", "음악 재생을 멈추고 큐를 비웁니다."),

    PAUSE("일시정지", "음악을 일시 정지 합니다."),

    RESUME("다시재생", "일시정지된 음악을 다시 재생합니다."),

    SKIP("다음곡", "현재 곡을 건너뛰고 다음 곡을 재생합니다."),

    NOW_PLAYING("현재곡", "지금 재생중인 곡의 정보를 보여줍니다."),

    FAVORITE_ADD("즐겨찾기추가", "지금 재생 중인 곡을 내 즐겨찾기에 저장합니다."),

    FAVORITE_LIST("즐겨찾기목록", "내 즐겨찾기 목록을 보여줍니다."),

    FAVORITE_REMOVE("즐겨찾기삭제", "내 즐겨찾기에서 곡을 삭제합니다.",
            new OptionData(OptionType.INTEGER, "number", "삭제할 곡 번호 (목록 명령어로 확인)", true));

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
