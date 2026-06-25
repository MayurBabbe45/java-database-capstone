package com.project.back_end.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

// @Document marks this class as a MongoDB document rather than a MySQL table
@Document(collection = "prescriptions")
public class Prescription {

    // MongoDB uses String ObjectIDs by default
    @Id
    private String id;
    
    // This acts as a manual reference back to the MySQL appointments table
    private Long appointmentId; 
    
    private String patientName;
    private List<String> medications;
    private String doctorNotes;

    // No-argument constructor
    public Prescription() {
    }

    public Prescription(Long appointmentId, String patientName, List<String> medications, String doctorNotes) {
        this.appointmentId = appointmentId;
        this.patientName = patientName;
        this.medications = medications;
        this.doctorNotes = doctorNotes;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Long getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Long appointmentId) { this.appointmentId = appointmentId; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public List<String> getMedications() { return medications; }
    public void setMedications(List<String> medications) { this.medications = medications; }

    public String getDoctorNotes() { return doctorNotes; }
    public void setDoctorNotes(String doctorNotes) { this.doctorNotes = doctorNotes; }
}