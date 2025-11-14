package com.example.mudy.study.service;

import com.example.mudy.study.model.Assignment;
import com.example.mudy.study.repository.AssignmentRepository;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;

    public AssignmentService(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    public Assignment registerAssignment(String userId, String title, LocalDateTime deadline, String userName, TextChannel channel) {
        Assignment assignment = new Assignment(userId, title, deadline, userName, channel);
        assignmentRepository.save(assignment);
        return assignment;
    }

    public List<Assignment> getUserAssignments(String userId) {
        return assignmentRepository.findByUserId(userId);
    }

    public void completeAssignment(String userId, String title) {
        assignmentRepository.complete(userId, title);
    }

    public void deleteAssignment(String userId, String title) {
        assignmentRepository.delete(userId, title);
    }
}
