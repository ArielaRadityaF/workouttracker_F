package com.example.workouttracker.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "progress")
public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int totalCalories;
    private int totalDuration;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Progress() {}

    /** Hitung total kalori & durasi dari daftar WorkoutSession milik user. */
    public void calculateTotal(List<WorkoutSession> sessions) {
        this.totalCalories = sessions.stream().mapToInt(WorkoutSession::getTotalCalories).sum();
        this.totalDuration = sessions.stream().mapToInt(WorkoutSession::getTotalDuration).sum();
    }

    public Long getId() { return id; }
    public int getTotalCalories() { return totalCalories; }
    public int getTotalDuration() { return totalDuration; }
    public User getUser() { return user; }

    public void setTotalCalories(int totalCalories) { this.totalCalories = totalCalories; }
    public void setTotalDuration(int totalDuration) { this.totalDuration = totalDuration; }
    public void setUser(User user) { this.user = user; }
}
