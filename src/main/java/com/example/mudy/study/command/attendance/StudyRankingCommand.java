package com.example.mudy.study.command.attendance;

import com.example.mudy.study.command.StudyCommand;
import com.example.mudy.study.model.UserAttend;
import com.example.mudy.study.service.AttendanceService;
import com.example.mudy.study.util.TimeTextGenerator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StudyRankingCommand extends ListenerAdapter {

    private final AttendanceService attendanceService;

    public StudyRankingCommand(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals(StudyCommand.RANKING.getName())) return;

        List<UserAttend> ranking = attendanceService.getUserRanking();

        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("스터디 랭킹");
        int rank = 1;
        for (UserAttend userAttend : ranking) {
            embed.addField(rank + "등 " + userAttend.getUserName() + "님",
                    "총 " + TimeTextGenerator.generate(userAttend.getTime()), false);
            rank++;
        }

        event.replyEmbeds(embed.build()).queue();
    }
}
