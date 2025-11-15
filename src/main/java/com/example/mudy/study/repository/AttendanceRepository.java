package com.example.mudy.study.repository;

import com.example.mudy.study.model.Attendance;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AttendanceRepository {
    @Getter
    private List<Attendance> attendances = new ArrayList<>();

    public void start(LocalDateTime startTime, String userId) {
        attendances.add(new Attendance(startTime, userId));
    }

    public void end(LocalDateTime endTime, String userId) {
        for (Attendance a : attendances) {
            if (a.getUserId().equals(userId)) {
                a.end(endTime);
            }
        }
    }
}
