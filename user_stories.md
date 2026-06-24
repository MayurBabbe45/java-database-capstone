# Smart Clinic Management System: User Stories

This document outlines the core Agile user stories for the Smart Clinic Management System, categorized by the three primary user roles: Admin, Doctor, and Patient.

## 1. Admin User Stories

**Story: ADM-01 | Manage Doctor Profiles**
* **As a** Clinic Administrator
* **I want to** add, update, and remove doctor profiles in the system
* **So that** the clinic's active roster is always accurate and up to date.
* **Acceptance Criteria:**
  - Admin can access a secure dashboard to view all doctors.
  - Admin can fill out a form to register a new doctor with their specialty and schedule.
  - Admin can deactivate a doctor's profile, preventing future bookings while retaining historical data.

**Story: ADM-02 | System-Wide Appointment Monitoring**
* **As a** Clinic Administrator
* **I want to** view a master list of all daily appointments across all doctors
* **So that** I can monitor clinic capacity, identify bottlenecks, and manage administrative staffing.
* **Acceptance Criteria:**
  - Admin can filter appointments by date, doctor, and status (e.g., Scheduled, Completed, Canceled).
  - The UI displays a consolidated, read-only view of the data.

## 2. Doctor User Stories

**Story: DOC-01 | View Daily Schedule**
* **As a** Doctor
* **I want to** view my upcoming appointments for the day upon logging in
* **So that** I can prepare for my consultations and manage my time effectively.
* **Acceptance Criteria:**
  - The doctor's dashboard displays a chronological list of today's appointments.
  - Each appointment card shows the patient's name, time slot, and reason for visit.

**Story: DOC-02 | Issue Prescriptions**
* **As a** Doctor
* **I want to** create and attach a digital prescription to a patient's record during or after an appointment
* **So that** the patient has a secure, permanent record of their treatment plan.
* **Acceptance Criteria:**
  - Doctor can open a text editor/form within an appointment view to write a prescription.
  - The prescription is saved to the MongoDB document store and linked to the patient's ID.

**Story: DOC-03 | Access Patient History**
* **As a** Doctor
* **I want to** view a patient's past appointments and prescriptions
* **So that** I can make informed diagnostic decisions based on their medical history.
* **Acceptance Criteria:**
  - Doctor can click on a patient's name to view a historical timeline of past visits and associated MongoDB prescription documents.

## 3. Patient User Stories

**Story: PAT-01 | Book an Appointment**
* **As a** Patient
* **I want to** browse available time slots for a specific doctor and book an appointment
* **So that** I can secure a consultation at a time that works for me.
* **Acceptance Criteria:**
  - Patient can select a doctor and view a calendar of available slots (fetching from MySQL).
  - Patient can confirm booking, which updates the database and prevents double-booking.

**Story: PAT-02 | View My Prescriptions**
* **As a** Patient
* **I want to** log in and view a list of my active and past prescriptions
* **So that** I know exactly what medications I have been prescribed without losing physical paper.
* **Acceptance Criteria:**
  - Patient dashboard includes a "My Records" section retrieving documents from MongoDB.
  - Patient cannot edit or delete these records (Read-Only access).