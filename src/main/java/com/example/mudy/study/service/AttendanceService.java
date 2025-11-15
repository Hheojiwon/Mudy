package com.example.mudy.study.service;

import com.example.mudy.study.model.Attendance;
import com.example.mudy.study.repository.AttendanceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AttendanceService {

    private AttendanceRepository attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    public void startAttendance(String userId) {
        attendanceRepository.start(LocalDateTime.now(), userId);
    }

    public Attendance endAttendance(String userId) {
        return attendanceRepository.end(LocalDateTime.now(), userId);
    }
}
