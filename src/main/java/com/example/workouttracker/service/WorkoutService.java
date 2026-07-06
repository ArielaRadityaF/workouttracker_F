package com.example.workouttracker.service;

import com.example.workouttracker.model.Exercise;
import com.example.workouttracker.model.Workout;
import com.example.workouttracker.repository.WorkoutRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkoutService {

    private final WorkoutRepository workoutRepository;

    public WorkoutService(WorkoutRepository workoutRepository) {
        this.workoutRepository = workoutRepository;
    }

    public List<Workout> getAllWorkouts() {
        return workoutRepository.findAll();
    }

    public Workout saveWorkout(Workout workout) {
        return workoutRepository.save(workout);
    }

    public Workout getWorkoutById(Long id) {
        return workoutRepository.findById(id).orElse(null);
    }

    public void deleteWorkout(Long id) {
        workoutRepository.deleteById(id);
    }

    public void addExerciseToWorkout(Long workoutId, Exercise exercise) {
        Workout workout = getWorkoutById(workoutId);
        if (workout != null) {
            // Mutate the existing managed collection in place (never replace it wholesale
            // on an attached entity when orphanRemoval = true is set).
            workout.getExercises().add(exercise);
            workoutRepository.save(workout);
        }
    }
}