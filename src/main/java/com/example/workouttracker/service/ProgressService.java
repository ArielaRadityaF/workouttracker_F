package com.example.workouttracker.service;

import com.example.workouttracker.model.Progress;
import com.example.workouttracker.repository.ProgressRepository;
import org.springframework.stereotype.Service;

@Service
public class ProgressService {

    private final ProgressRepository progressRepository;

    public ProgressService(ProgressRepository progressRepository) {
        this.progressRepository = progressRepository;
    }

    public Progress getProgressByUserId(Long userId) {
        return progressRepository.findByUserId(userId).orElse(null);
    }

    public Progress saveProgress(Progress progress) {
        return progressRepository.save(progress);
    }
}
