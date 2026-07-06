package com.example.workouttracker.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("STRENGTH")
public class StrengthWorkout extends Workout {

    private double weight;
    private int reps;
    private int sets;

    public StrengthWorkout() {}

    public StrengthWorkout(String name, double weight, int reps, int sets) {
        // Strength workouts are logged by weight/reps/sets rather than duration
        super(name, 0);
        this.weight = weight;
        this.reps = reps;
        this.sets = sets;
    }

    @Override
    public int calculateCalories() {
        // Simple estimate based on total training volume (weight x reps x sets)
        return (int) Math.round(weight * reps * sets * 0.05);
    }

    @Override
    public String getWorkoutType() {
        return "Strength";
    }

    @Override
    public String getSummary() {
        String weightText = (weight == Math.floor(weight))
                ? String.valueOf((long) weight)
                : String.valueOf(weight);
        return weightText + " kg x " + reps + " reps x " + sets + " sets";
    }

    public double getWeight() { return weight; }
    public int getReps() { return reps; }
    public int getSets() { return sets; }

    public void setWeight(double weight) { this.weight = weight; }
    public void setReps(int reps) { this.reps = reps; }
    public void setSets(int sets) { this.sets = sets; }
}
