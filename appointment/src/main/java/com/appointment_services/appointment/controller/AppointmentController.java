package com.appointment_services.appointment.controller;

import com.appointment_services.appointment.entity.Appointment;
import com.appointment_services.appointment.payload.Doctor;
import com.appointment_services.appointment.payload.Patient;
import com.appointment_services.appointment.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.print.Doc;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {


    private final AppointmentService appointmentService;
    private final RestTemplate restTemplate;

    @Autowired
    public AppointmentController(RestTemplate restTemplate, AppointmentService appointmentService){
        this.restTemplate = restTemplate;
        this.appointmentService = appointmentService;

    }

    // Get all appointments
    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    // Get appointment by ID
    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        Optional<Appointment> appointment = appointmentService.getAppointmentById(id);
        return appointment.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a new appointment
    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment) {
        ResponseEntity<Patient> patientResponse = restTemplate.getForEntity("http://patient-service/api/patients/"+appointment.getPatientId(), Patient.class);
        if(patientResponse.getStatusCode() != HttpStatus.OK || patientResponse.getBody() == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Patient patient = patientResponse.getBody();

        ResponseEntity<Doctor> doctorResponse = restTemplate.getForEntity("http://doctor/api/doctors/"+appointment.getDoctorId(), Doctor.class);
        if(doctorResponse.getStatusCode() != HttpStatus.OK || doctorResponse.getBody() == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Doctor doctor = doctorResponse.getBody();

        Appointment createdAppointment = appointmentService.saveAppointment(appointment);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAppointment);
    }

    // Update an appointment
    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable Long id, @RequestBody Appointment appointmentDetails) {
        Appointment updatedAppointment = appointmentService.updateAppointment(id, appointmentDetails);
        return ResponseEntity.ok(updatedAppointment);
    }

    // Delete an appointment
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }
}


