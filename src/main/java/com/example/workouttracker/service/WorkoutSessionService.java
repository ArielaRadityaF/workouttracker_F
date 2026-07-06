package com.example.workouttracker.service;

import com.example.workouttracker.model.WorkoutSession;
import com.example.workouttracker.repository.WorkoutSessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkoutSessionService {

    private final WorkoutSessionRepository sessionRepository;

    public WorkoutSessionService(WorkoutSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public WorkoutSession saveSession(WorkoutSession session) {
        return sessionRepository.save(session);
    }

    public List<WorkoutSession> getAllSessions() {
        return sessionRepository.findAll();
    }

    public WorkoutSession getSessionById(Long id) {
        return sessionRepository.findById(id).orElse(null);
    }

    public void deleteSession(Long id) {
        sessionRepository.deleteById(id);
    }
}