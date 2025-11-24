package com.example.mudy.study.repository;

import com.example.mudy.study.model.Attendance;
import com.example.mudy.study.model.UserAttend;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Repository
public class AttendanceRepository {
    private List<Attendance> attendances = new ArrayList<>();
    private List<UserAttend> userAttends = new ArrayList<>();

    public void saveAttendance(Attendance attendance) {
        attendances.add(attendance);
    }

    public void saveUserAttend(UserAttend userAttend) {
        userAttends.add(userAttend);
    }

    public boolean existsUserAttend(String userId) {
        return userAttends.stream().anyMatch(attendance -> attendance.getUserId().equals(userId));
    }

    public List<UserAttend> findAllUserAttends() {
        return userAttends.stream()
                .sorted(Comparator.comparing(UserAttend::getTime).reversed()).toList();
    }

    public Attendance endByUserId(String userId) {
        Attendance attendance = null;

        for (Attendance a : attendances) {
            if (a.getUserId().equals(userId) && !a.getStartTime().equals(LocalDateTime.now())) {
                a.end(LocalDateTime.now());
                attendance = a;
            }
        }

        return attendance;
    }

    public void addTimeByUserId(String userId, Duration time) {
        for (UserAttend userAttend : userAttends) {
            if (userAttend.getUserId().equals(userId)) {
                userAttend.addTime(time);
            }
        }
    }
}
