package com.example.workouttracker.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workout")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "workout_type", discriminatorType = DiscriminatorType.STRING)
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int duration;

    // FK "workout_id" langsung di tabel exercise, tanpa join table tersembunyi
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "workout_id")
    private List<Exercise> exercises = new ArrayList<>();

    public Workout() {}

    public Workout(String name, int duration) {
        this.name = name;
        this.duration = duration;
    }

    public int calculateCalories() {
        return 0;
    }

    public String getWorkoutType() {
        return "Workout";
    }

    /** Short human-readable summary of the workout-specific metrics (used in the UI). */
    public String getSummary() {
        return duration + " min";
    }

    /** Tampilkan info workout (tipe, nama, ringkasan). */
    public String display() {
        return getWorkoutType() + " - " + name + " (" + getSummary() + ")";
    }

    /** Tambahkan satu Exercise ke workout ini. */
    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public int getDuration() { return duration; }
    public List<Exercise> getExercises() { return exercises; }

    public void setName(String name) { this.name = name; }
    public void setDuration(int duration) { this.duration = duration; }
    public void setExercises(List<Exercise> exercises) { this.exercises = exercises; }
}
