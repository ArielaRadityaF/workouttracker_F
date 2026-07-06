package com.example.workouttracker.controller;

import com.example.workouttracker.model.*;
import com.example.workouttracker.service.ProgressService;
import com.example.workouttracker.service.UserService;
import com.example.workouttracker.service.WorkoutService;
import com.example.workouttracker.service.WorkoutSessionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class WorkoutController {

    private final WorkoutSessionService workoutSessionService;
    private final WorkoutService workoutService;
    private final UserService userService;
    private final ProgressService progressService;

    public WorkoutController(WorkoutSessionService workoutSessionService,
                              WorkoutService workoutService,
                              UserService userService,
                              ProgressService progressService) {
        this.workoutSessionService = workoutSessionService;
        this.workoutService = workoutService;
        this.userService = userService;
        this.progressService = progressService;
    }

    @GetMapping("/")
    public String home(Model model) {
        List<WorkoutSession> sessions = workoutSessionService.getAllSessions();
        List<User> users = userService.getAllUsers();

        model.addAttribute("sessions", sessions);
        model.addAttribute("users", users);

        int totalSessions = sessions.size();
        int totalCalories = sessions.stream().mapToInt(WorkoutSession::getTotalCalories).sum();
        int totalDuration = sessions.stream().mapToInt(WorkoutSession::getTotalDuration).sum();
        long totalWorkouts = sessions.stream().mapToLong(s -> s.getWorkouts().size()).sum();

        model.addAttribute("totalSessions", totalSessions);
        model.addAttribute("totalCalories", totalCalories);
        model.addAttribute("totalDuration", totalDuration);
        model.addAttribute("totalWorkouts", totalWorkouts);
        model.addAttribute("totalUsers", users.size());

        // Map userId -> Progress, so the dashboard can show each user's latest calculated progress
        Map<Long, Progress> progressMap = new HashMap<>();
        for (User user : users) {
            Progress progress = progressService.getProgressByUserId(user.getId());
            if (progress != null) {
                progressMap.put(user.getId(), progress);
            }
        }
        model.addAttribute("progressMap", progressMap);

        return "index";
    }

    @PostMapping("/add")
    public String addWorkout(
            @RequestParam String name,
            @RequestParam String type,
            @RequestParam String date,
            @RequestParam(required = false) Integer duration,
            @RequestParam(required = false) Double weight,
            @RequestParam(required = false) Integer reps,
            @RequestParam(required = false) Integer sets,
            @RequestParam(required = false) Long userId
    ) {
        Workout workout = buildWorkout(type, name, duration, weight, reps, sets);

        WorkoutSession session = new WorkoutSession();
        session.setDate(date);
        session.addWorkout(workout);

        if (userId != null) {
            User user = userService.getUserById(userId);
            if (user != null) {
                // Mutate the user's managed collection in place; cascade = ALL on User.sessions
                // will persist the new session (and its workout) together with the user.
                user.addSession(session);
                userService.saveUser(user);
                return "redirect:/";
            }
        }

        workoutSessionService.saveSession(session);
        return "redirect:/";
    }

    @GetMapping("/delete-session/{id}")
    public String deleteSession(@PathVariable Long id) {
        workoutSessionService.deleteSession(id);
        return "redirect:/";
    }

    @PostMapping("/add-user")
    public String addUser(@RequestParam String name) {
        if (name != null && !name.isBlank()) {
            userService.saveUser(new User(name.trim()));
        }
        return "redirect:/";
    }

    @PostMapping("/generate-progress")
    public String generateProgress(@RequestParam Long userId) {
        User user = userService.getUserById(userId);

        if (user != null) {
            Progress progress = progressService.getProgressByUserId(userId);
            if (progress == null) {
                progress = new Progress();
                progress.setUser(user);
            }
            progress.calculateTotal(user.getSessions());
            progressService.saveProgress(progress);
        }

        return "redirect:/";
    }

    @PostMapping("/add-exercise")
    public String addExercise(
            @RequestParam Long workoutId,
            @RequestParam String name,
            @RequestParam(required = false) Integer reps
    ) {
        Exercise exercise = new Exercise(name, reps != null ? reps : 0);
        workoutService.addExerciseToWorkout(workoutId, exercise);
        return "redirect:/";
    }

    private Workout buildWorkout(String type, String name, Integer duration, Double weight, Integer reps, Integer sets) {
        if ("cardio".equals(type)) {
            int d = duration != null ? duration : 0;
            return new CardioWorkout(name, d);
        } else {
            double w = weight != null ? weight : 0;
            int r = reps != null ? reps : 0;
            int s = sets != null ? sets : 0;
            return new StrengthWorkout(name, w, r, s);
        }
    }
}
