package com.example.mudy.study.command.attendance;

import com.example.mudy.study.command.StudyCommand;
import com.example.mudy.study.model.Attendance;
import com.example.mudy.study.service.AttendanceService;
import com.example.mudy.study.util.TimeTextGenerator;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class StudyEndCommand extends ListenerAdapter {

    private final AttendanceService attendanceService;

    public StudyEndCommand(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (!event.getName().equals(StudyCommand.END.getName())) return;

        try {
            Attendance attendance = attendanceService.endAttendance(event.getUser().getId());
            event.reply(TimeTextGenerator.generate(Duration.between(attendance.getStartTime(), attendance.getEndTime()))
                    + " 동안 스터디에 참여했습니다.").queue();
        } catch (Exception e) {
            event.reply("🟥 스터디가 시작되지 않았습니다.").queue();
        }
    }
}
