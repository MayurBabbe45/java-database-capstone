package com.project.back_end.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Doctor name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Specialty is required")
    @Column(nullable = false)
    private String specialty;

    // Automatically creates a secondary table to store the list of strings
    @ElementCollection
    private List<String> availableTimes;

    // No-argument constructor required by JPA
    public Doctor() {
    }

    public Doctor(String name, String specialty, List<String> availableTimes) {
        this.name = name;
        this.specialty = specialty;
        this.availableTimes = availableTimes;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }

    public List<String> getAvailableTimes() { return availableTimes; }
    public void setAvailableTimes(List<String> availableTimes) { this.availableTimes = availableTimes; }
}