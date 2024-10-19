package apap.ti.hospitalization2206818953.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apap.ti.hospitalization2206818953.model.Patient;
import apap.ti.hospitalization2206818953.repository.PatientDb;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PatientServiceImpl implements PatientService {
    
    @Autowired
    private PatientDb patientDb;

    @Override
    public Patient addPatient(Patient patient) {
        return patientDb.save(patient);
    }

    @Override
    public List<Patient> getAllPatients() {
        return patientDb.findAll();
    }

    @Override
    public Patient getPatientById(UUID idPatient) {
        return patientDb.findById(idPatient).orElse(null);
    }

    @Override
    public Patient updatePatient(Patient patient) {
        Patient existingPatient = getPatientById(patient.getId());
        if (existingPatient != null) {
            existingPatient.setName(patient.getName());
            existingPatient.setEmail(patient.getEmail());
            existingPatient.setNIK(patient.getNIK());
            existingPatient.setBirthDate(patient.getBirthDate());
            existingPatient.setGender(patient.isGender());
            patientDb.save(existingPatient);
            return existingPatient;
        }
        return null;
    }

    @Override
    public void deletePatient(Patient patient) {
        patientDb.delete(patient);
    }

    @Override
    public int countPatients() {
        return (int) patientDb.count();
    }

    @Override
    public Patient getPatientByNIK(String nik) {
        return patientDb.findByNIK(nik);
    }
}