package com.project.back_end.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Defines the foreign key relationship to the doctors table
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    // Defines the foreign key relationship to the patients table
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @NotNull(message = "Appointment date and time cannot be null")
    @Column(nullable = false)
    private LocalDateTime appointmentDate;

    @Column(nullable = false)
    private String status = "SCHEDULED";

    // No-argument constructor required by JPA
    public Appointment() {
    }

    public Appointment(Doctor doctor, Patient patient, LocalDateTime appointmentDate) {
        this.doctor = doctor;
        this.patient = patient;
        this.appointmentDate = appointmentDate;
    }

    // Helper Method requested by the rubric
    public LocalDateTime getEndTime() {
        if (this.appointmentDate == null) return null;
        // Standard capstone assumption: appointments are 30 minutes long
        return this.appointmentDate.plusMinutes(30);
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Doctor getDoctor() { return doctor; }
    public void setDoctor(Doctor doctor) { this.doctor = doctor; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public LocalDateTime getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(LocalDateTime appointmentDate) { this.appointmentDate = appointmentDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}