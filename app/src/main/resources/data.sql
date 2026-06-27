-- ==========================================
-- CLINIC MANAGEMENT SYSTEM (CMS)
-- INITIAL SAMPLE DATA
-- ==========================================

-- Insert Admins
INSERT INTO admins (username, password) VALUES
('admin_mayur', 'hashed_password_123'),
('admin_sarah', 'hashed_password_456');

-- Insert Doctors
INSERT INTO doctors (name, specialty) VALUES
('Dr. Alice Smith', 'Cardiology'),
('Dr. Bob Jones', 'Pediatrics'),
('Dr. Clara Oswald', 'Neurology');

-- Insert Doctor Available Times
INSERT INTO doctor_available_times (doctor_id, available_times) VALUES
(1, '09:00 AM'), (1, '10:00 AM'), (1, '02:00 PM'),
(2, '11:00 AM'), (2, '01:00 PM'),
(3, '08:00 AM'), (3, '04:00 PM');

-- Insert Patients
INSERT INTO patients (name, email, address) VALUES
('John Doe', 'john.doe@example.com', '123 Elm St, Pune'),
('Jane Roe', 'jane.roe@example.com', '456 Oak St, Pune');

-- Insert Appointments
-- Mixing dates to ensure monthly and yearly reports have valid test data
INSERT INTO appointments (doctor_id, patient_id, appointment_date, status) VALUES
(1, 1, '2026-06-26 09:00:00', 'SCHEDULED'),
(2, 2, '2026-06-27 11:00:00', 'SCHEDULED'),
(3, 1, '2026-06-28 16:00:00', 'COMPLETED'),
(1, 2, '2026-06-26 10:00:00', 'COMPLETED'),
(1, 1, '2026-07-15 09:00:00', 'SCHEDULED'),
(2, 1, '2026-07-15 11:00:00', 'SCHEDULED'),
(2, 2, '2026-07-16 14:00:00', 'COMPLETED'),
(2, 1, '2025-12-05 10:00:00', 'COMPLETED');


-- ==========================================
-- STORED PROCEDURES FOR REPORTING
-- ==========================================

DELIMITER //

-- 1. Daily appointments grouped by doctor
DROP PROCEDURE IF EXISTS GetDailyAppointmentReportByDoctor //
CREATE PROCEDURE GetDailyAppointmentReportByDoctor(IN target_date DATE)
BEGIN
    SELECT d.name AS DoctorName, COUNT(a.id) AS TotalAppointments
    FROM doctors d
    JOIN appointments a ON d.id = a.doctor_id
    WHERE DATE(a.appointment_date) = target_date
    GROUP BY d.id, d.name;
END //

-- 2. Doctor who saw the most unique patients in a specific month
DROP PROCEDURE IF EXISTS GetDoctorWithMostPatientsByMonth //
CREATE PROCEDURE GetDoctorWithMostPatientsByMonth(IN target_month INT, IN target_year INT)
BEGIN
    SELECT d.name AS DoctorName, COUNT(DISTINCT a.patient_id) AS UniquePatientsSeen
    FROM doctors d
    JOIN appointments a ON d.id = a.doctor_id
    WHERE MONTH(a.appointment_date) = target_month AND YEAR(a.appointment_date) = target_year
    GROUP BY d.id, d.name
    ORDER BY UniquePatientsSeen DESC
    LIMIT 1;
END //

-- 3. Doctor who saw the most unique patients in a specific year
DROP PROCEDURE IF EXISTS GetDoctorWithMostPatientsByYear //
CREATE PROCEDURE GetDoctorWithMostPatientsByYear(IN target_year INT)
BEGIN
    SELECT d.name AS DoctorName, COUNT(DISTINCT a.patient_id) AS UniquePatientsSeen
    FROM doctors d
    JOIN appointments a ON d.id = a.doctor_id
    WHERE YEAR(a.appointment_date) = target_year
    GROUP BY d.id, d.name
    ORDER BY UniquePatientsSeen DESC
    LIMIT 1;
END //

DELIMITER ;