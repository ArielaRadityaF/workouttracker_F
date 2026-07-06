package com.example.workouttracker.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "app_user") // "user" dihindari karena kata reserved di beberapa database (termasuk H2)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // FK "user_id" langsung di tabel workout_session, tanpa join table tersembunyi
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<WorkoutSession> sessions = new ArrayList<>();

    public User() {}

    public User(String name) {
        this.name = name;
    }

    /** Tambahkan satu WorkoutSession ke user ini. */
    public void addSession(WorkoutSession session) {
        sessions.add(session);
    }

    /** Tampilkan semua WorkoutSession milik user ini. */
    public List<WorkoutSession> showSessions() {
        return sessions;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public List<WorkoutSession> getSessions() { return sessions; }

    public void setName(String name) { this.name = name; }
    public void setSessions(List<WorkoutSession> sessions) { this.sessions = sessions; }
}
