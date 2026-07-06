package com.example.workouttracker.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("CARDIO")
public class CardioWorkout extends Workout {

    public CardioWorkout() {}

    public CardioWorkout(String name, int duration) {
        super(name, duration);
    }

    @Override
    public int calculateCalories() {
        // Simple estimate: ~8 kcal burned per minute of cardio
        return getDuration() * 8;
    }

    @Override
    public String getWorkoutType() {
        return "Cardio";
    }

    @Override
    public String getSummary() {
        return getDuration() + " min";
    }
}
