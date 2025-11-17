package com.example.mudy.study.service;

import com.example.mudy.study.model.Attendance;
import com.example.mudy.study.model.UserAttend;
import com.example.mudy.study.repository.AttendanceRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AttendanceService {

    private AttendanceRepository attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    public void startAttendance(String userId, String userName) {
        attendanceRepository.saveAttendance(new Attendance(LocalDateTime.now(), userId));
        if (!attendanceRepository.existsUserAttend(userId)) {
            attendanceRepository.saveUserAttend(new UserAttend(userId, userName));
        }
    }

    public Attendance endAttendance(String userId) {
        Attendance attendance = attendanceRepository.endByUserId(userId);
        attendanceRepository.addTimeByUserId(userId, Duration.between(attendance.getStartTime(), attendance.getEndTime()));
        return attendance;
    }

    public List<UserAttend> getUserRanking() {
        return attendanceRepository.findAllUserAttends();
    }
}
