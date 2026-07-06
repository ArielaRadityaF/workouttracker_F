package com.example.workouttracker.model;

import jakarta.persistence.*;

@Entity
@Table(name = "exercise")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private int reps;

    public Exercise() {}

    public Exercise(String name, int reps) {
        this.name = name;
        this.reps = reps;
    }

    /** Tampilkan info gerakan (name + reps). */
    public String display() {
        return name + " x " + reps + " reps";
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public int getReps() { return reps; }

    public void setName(String name) { this.name = name; }
    public void setReps(int reps) { this.reps = reps; }
}
