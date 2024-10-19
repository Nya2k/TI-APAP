package apap.ti.hospitalization2206818953.service;

import java.util.List;
import java.util.UUID;

import apap.ti.hospitalization2206818953.model.Patient;

public interface PatientService {
    Patient addPatient(Patient patient);
    List<Patient> getAllPatients();
    Patient getPatientById(UUID idPatient);
    Patient updatePatient(Patient patient);
    void deletePatient(Patient patient);
    int countPatients();
    Patient getPatientByNIK(String nik);
}
