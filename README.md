# Workout Tracker

Aplikasi web untuk mencatat sesi latihan (cardio & strength), melacak kalori terbakar, mengelola beberapa user, dan memantau progress masing-masing secara otomatis. Dibangun sebagai studi kasus penerapan konsep Object-Oriented Programming (OOP) dalam aplikasi nyata menggunakan Java dan Spring Boot.

## Fitur

- Dashboard interaktif dengan ringkasan statistik (total sesi, workout, menit, kalori, user) dan grafik tren kalori
- Form dinamis untuk menambah workout, menyesuaikan field berdasarkan tipe Cardio atau Strength
- Manajemen user dan penugasan sesi latihan ke user tertentu
- Perhitungan progress otomatis (total kalori dan durasi) untuk setiap user
- Pencatatan exercise (gerakan spesifik beserta jumlah repetisi) di dalam setiap workout
- Pencarian dan filter workout berdasarkan nama atau tipe

## Konsep OOP yang Diterapkan

| Konsep | Penerapan |
|---|---|
| Inheritance | `CardioWorkout` dan `StrengthWorkout` mewarisi `Workout` |
| Polymorphism | `calculateCalories()` di-override berbeda di tiap subclass, dipanggil secara seragam lewat referensi `Workout` |
| Encapsulation | Semua atribut bersifat private, diakses lewat getter dan setter |
| Association | `User` memiliki banyak `WorkoutSession`, yang memiliki banyak `Workout`, yang memiliki banyak `Exercise` |

## Tech Stack

- Backend: Java 17, Spring Boot 3.5, Spring Data JPA (Hibernate)
- Frontend: Thymeleaf, Bootstrap 5, Chart.js
- Database: PostgreSQL (production), H2 in-memory (development)
- Deployment: Railway

## Struktur Class

```
Workout (parent)
├── CardioWorkout   : duration, calculateCalories() = duration x 8
└── StrengthWorkout : weight, reps, sets, calculateCalories() = weight x reps x sets x 0.05

WorkoutSession : berisi banyak Workout
Workout        : berisi banyak Exercise
User           : berisi banyak WorkoutSession, memiliki satu Progress
Progress       : totalCalories dan totalDuration, dihitung dari seluruh WorkoutSession milik User
```

## Struktur Database

| Tabel | Keterangan |
|---|---|
| workout | Menyimpan CardioWorkout dan StrengthWorkout dalam satu tabel, dibedakan lewat kolom workout_type |
| workout_session | Sesi latihan, berisi tanggal dan daftar workout |
| app_user | Data user |
| progress | Ringkasan total kalori dan durasi per user |
| exercise | Gerakan spesifik di dalam sebuah workout |

Relasi antar tabel menggunakan foreign key langsung (misalnya session_id di tabel workout), tanpa join table tersembunyi.

## Endpoint / Routing

Aplikasi ini menggunakan pola server-side rendering (Spring MVC dengan Thymeleaf), bukan REST API murni berbasis JSON setiap endpoint memproses form HTML dan mengembalikan halaman web (redirect ke dashboard), bukan response JSON.

| Method | Path | Deskripsi |
|---|---|---|
| GET | `/` | Menampilkan dashboard beserta seluruh data sesi, user, dan progress |
| POST | `/add` | Menambahkan workout session baru beserta satu workout di dalamnya |
| GET | `/delete-session/{id}` | Menghapus sebuah workout session beserta seluruh workout di dalamnya |
| POST | `/add-user` | Menambahkan user baru |
| POST | `/generate-progress` | Menghitung ulang dan menyimpan progress (total kalori dan durasi) untuk user tertentu |
| POST | `/add-exercise` | Menambahkan exercise ke sebuah workout tertentu |

Parameter form yang diterima masing-masing endpoint POST dapat dilihat langsung pada `WorkoutController.java`.

## Environment Variables

Environment variable berikut digunakan saat aplikasi berjalan dengan profile production (`application-prod.properties`), diisi lewat pengaturan environment variable di platform hosting (Railway):

| Variable | Deskripsi |
|---|---|
| `SPRING_PROFILES_ACTIVE` | Diisi `prod` agar aplikasi memakai konfigurasi PostgreSQL, bukan H2 |
| `PGHOST` | Host database PostgreSQL |
| `PGPORT` | Port database PostgreSQL |
| `PGDATABASE` | Nama database PostgreSQL |
| `PGUSER` | Username database PostgreSQL |
| `PGPASSWORD` | Password database PostgreSQL |
| `PORT` | Port yang digunakan aplikasi untuk menerima request, otomatis diisi oleh platform hosting (default 8080 jika tidak diisi) |

Saat dijalankan secara lokal tanpa environment variable ini, aplikasi otomatis memakai `application.properties` (H2 in-memory), sehingga tidak diperlukan konfigurasi tambahan apa pun.

## Menjalankan Secara Lokal

Prasyarat: Java 17 atau lebih baru, serta Maven (atau gunakan wrapper mvnw yang sudah disediakan).

```
./mvnw spring-boot:run
```

Aplikasi berjalan di `http://localhost:8080`, menggunakan database H2 in-memory (data reset setiap restart, cocok untuk development).

Konsol database H2 dapat diakses di `http://localhost:8080/h2-console` dengan kredensial dari `application.properties`.

## Deployment

Project ini dikonfigurasi untuk deploy ke Railway (https://railway.com) dengan PostgreSQL sebagai database production.

1. Push repository ini ke GitHub
2. Deploy dari Railway melalui opsi Deploy from GitHub repo
3. Tambahkan service PostgreSQL pada project Railway yang sama
4. Atur environment variable pada service aplikasi sesuai tabel Environment Variables di atas

Konfigurasi production terdapat pada `src/main/resources/application-prod.properties`.

## Struktur Project

```
src/main/java/com/example/workouttracker/
├── controller/   : WorkoutController, menangani request HTTP
├── model/        : Entity JPA (Workout, CardioWorkout, StrengthWorkout,
│                   WorkoutSession, User, Progress, Exercise)
├── repository/   : Interface Spring Data JPA untuk setiap entity
└── service/      : Business logic untuk setiap entity

src/main/resources/
├── templates/                   : index.html (Thymeleaf)
├── application.properties       : konfigurasi lokal (H2)
└── application-prod.properties  : konfigurasi production (PostgreSQL)
```

## Lisensi

Project ini dibuat untuk keperluan tugas akademik mata kuliah Object-Oriented Programming.
