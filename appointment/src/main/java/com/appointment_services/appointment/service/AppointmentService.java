package com.appointment_services.appointment.service;


import com.appointment_services.appointment.entity.Appointment;
import com.appointment_services.appointment.payload.Doctor;
import com.appointment_services.appointment.payload.Patient;
import com.appointment_services.appointment.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {


    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository){
        this.appointmentRepository = appointmentRepository;
    }


    // Get all appointments
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    // Get an appointment by ID
    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id);
    }


    // Create a new appointment by fetching details from Doctor and Patient services
    public Appointment saveAppointment(Appointment appointment) {
            return appointmentRepository.save(appointment);
    }

    // Update an appointment
    public Appointment updateAppointment(Long id, Appointment appointmentDetails) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setDoctorId(appointmentDetails.getDoctorId());
        appointment.setPatientId(appointmentDetails.getPatientId());
        appointment.setAppointmentDate(appointmentDetails.getAppointmentDate());
        appointment.setStatus(appointmentDetails.getStatus());

        return appointmentRepository.save(appointment);
    }

    // Delete an appointment
    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }
}


