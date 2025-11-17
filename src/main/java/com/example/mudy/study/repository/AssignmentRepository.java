package com.example.mudy.study.repository;

import com.example.mudy.study.model.Assignment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
public class AssignmentRepository {
    private final List<Assignment> assignments = new ArrayList<>();

    public void save(Assignment assignment) {
        assignments.add(assignment);
    }

    public List<Assignment> findByUserId(String userId) {
        List<Assignment> assignments = new ArrayList<>();
        for (Assignment a : this.assignments){
            if (a.getUserId().equals(userId)) { assignments.add(a); }
        }
        return assignments;
    }

    public List<Assignment> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(assignments));
    }

    public void complete(String userId, String title) {
        assignments.forEach(assignment -> {
            if (assignment.getUserId().equals(userId) && assignment.getTitle().equals(title)) { assignment.setCompleted(); }
        });
    }

    public void delete(String userId, String title) {
        assignments.removeIf(assignment ->
                assignment.getUserId().equals(userId) && assignment.getTitle().equals(title)
        );
    }
}
