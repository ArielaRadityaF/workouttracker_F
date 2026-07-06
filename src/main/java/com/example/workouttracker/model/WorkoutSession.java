package com.example.workouttracker.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workout_session")
public class WorkoutSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String date;

    // FK column "session_id" lives directly on the workout table (no hidden join table)
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "session_id")
    private List<Workout> workouts = new ArrayList<>();

    public WorkoutSession() {}

    public int getTotalCalories() {
        return workouts.stream().mapToInt(Workout::calculateCalories).sum();
    }

    public int getTotalDuration() {
        return workouts.stream().mapToInt(Workout::getDuration).sum();
    }

    /** Tambahkan satu Workout ke session ini. */
    public void addWorkout(Workout workout) {
        workouts.add(workout);
    }

    /** Tampilkan semua Workout dalam session ini. */
    public List<Workout> showWorkouts() {
        return workouts;
    }

    public Long getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public List<Workout> getWorkouts() {
        return workouts;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setWorkouts(List<Workout> workouts) {
        this.workouts = workouts;
    }
}
