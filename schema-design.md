# Smart Clinic Management System: Database Schema Design

This document outlines the polyglot database architecture for the Smart Clinic Management System. It utilizes a relational model (MySQL) for rigid, transactional scheduling data and a document model (MongoDB) for flexible clinical records.

## 1. Relational Database Design (MySQL)
The relational schema ensures ACID compliance for critical clinic operations. It consists of four primary tables to manage personnel and scheduling.

### Table: `admins`
Stores credentials and contact information for clinic administrators.
* **`id`** (BIGINT, PRIMARY KEY, AUTO_INCREMENT)
* **`username`** (VARCHAR(50), UNIQUE, NOT NULL)
* **`password_hash`** (VARCHAR(255), NOT NULL) - *Comment: Stores bcrypt hashed passwords, never plaintext.*
* **`email`** (VARCHAR(100), UNIQUE, NOT NULL)
* **`created_at`** (TIMESTAMP, DEFAULT CURRENT_TIMESTAMP)

### Table: `doctors`
Stores doctor profiles, specializations, and availability status.
* **`id`** (BIGINT, PRIMARY KEY, AUTO_INCREMENT)
* **`first_name`** (VARCHAR(50), NOT NULL)
* **`last_name`** (VARCHAR(50), NOT NULL)
* **`specialization`** (VARCHAR(100), NOT NULL)
* **`email`** (VARCHAR(100), UNIQUE, NOT NULL)
* **`is_active`** (BOOLEAN, DEFAULT TRUE) - *Comment: Implements soft-deletes to retain historical appointment integrity if a doctor leaves.*
* **`created_at`** (TIMESTAMP, DEFAULT CURRENT_TIMESTAMP)

### Table: `patients`
Stores patient demographics and contact details.
* **`id`** (BIGINT, PRIMARY KEY, AUTO_INCREMENT)
* **`first_name`** (VARCHAR(50), NOT NULL)
* **`last_name`** (VARCHAR(50), NOT NULL)
* **`date_of_birth`** (DATE, NOT NULL)
* **`phone_number`** (VARCHAR(15), UNIQUE)
* **`is_active`** (BOOLEAN, DEFAULT TRUE)
* **`created_at`** (TIMESTAMP, DEFAULT CURRENT_TIMESTAMP)

### Table: `appointments`
The junction table handling the transactional booking logic between patients and doctors.
* **`id`** (BIGINT, PRIMARY KEY, AUTO_INCREMENT)
* **`patient_id`** (BIGINT, NOT NULL) - *FOREIGN KEY references patients(id)*
* **`doctor_id`** (BIGINT, NOT NULL) - *FOREIGN KEY references doctors(id)*
* **`appointment_date`** (DATETIME, NOT NULL)
* **`status`** (ENUM('SCHEDULED', 'COMPLETED', 'CANCELED'), DEFAULT 'SCHEDULED')
* **`reason_for_visit`** (TEXT)
* **`updated_at`** (TIMESTAMP, DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)

---

## 2. Document Database Design (MongoDB)
Unstructured and semi-structured clinical data is stored in MongoDB. This allows for flexible schemas where medications and notes can vary vastly from patient to patient without requiring complex SQL joins.

### Collection: `prescriptions`
This collection stores the medical outcomes of an appointment. It maps back to the MySQL relational data via the `patientId` and `doctorId` fields.

**Realistic JSON Document Example:**
```json
{
  "_id": {
    "$oid": "64b8f0c2a8c9e12b3c4d5e6f"
  },
  "patientId": 1042,     // Maps to MySQL patients.id
  "doctorId": 7,         // Maps to MySQL doctors.id
  "appointmentId": 8831, // Maps to MySQL appointments.id
  "issueDate": "2026-06-25T09:00:00Z",
  "diagnoses": [
    "Acute Bronchitis",
    "Mild Hypertension"
  ],
  "medications": [
    {
      "name": "Amoxicillin",
      "dosage": "500mg",
      "frequency": "Every 8 hours",
      "durationInDays": 7,
      "specialInstructions": "Take with food to avoid nausea."
    },
    {
      "name": "Lisinopril",
      "dosage": "10mg",
      "frequency": "Once daily",
      "durationInDays": 30,
      "specialInstructions": "Take in the morning."
    }
  ],
  "doctorNotes": "Patient experiencing persistent cough. Advised rest and hydration. Follow up in 2 weeks if symptoms persist.",
  "followUpRequired": true
}