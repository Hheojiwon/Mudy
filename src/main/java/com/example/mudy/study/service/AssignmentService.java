package com.example.mudy.study.service;

import com.example.mudy.study.model.Assignment;
import com.example.mudy.study.repository.AssignmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;

    public AssignmentService(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    public Assignment registerAssignment(String userId, String title, LocalDateTime deadline) {
        Assignment assignment = new Assignment(userId, title, deadline);
        assignmentRepository.save(assignment);
        return assignment;
    }

    public List<Assignment> getUserAssignments(String userId) {
        return assignmentRepository.findByUserId(userId);
    }
}
