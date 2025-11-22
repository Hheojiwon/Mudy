package com.example.mudy.study.command.attendance;

import com.example.mudy.study.command.StudyCommand;
import com.example.mudy.study.service.AttendanceService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
public class StudyAttendCommand extends ListenerAdapter {

    private final AttendanceService attendanceService;

    public StudyAttendCommand(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event){
        if (!event.getName().equals(StudyCommand.ATTEND.getName())) return;

        attendanceService.startAttendance(event.getUser().getId(), event.getUser().getGlobalName());

        event.reply("✅ " + event.getUser().getGlobalName() + "님이 출석하였습니다.").queue();
    }
}
