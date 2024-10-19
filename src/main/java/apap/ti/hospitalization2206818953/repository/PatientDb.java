package apap.ti.hospitalization2206818953.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import apap.ti.hospitalization2206818953.model.Patient;

@Repository
public interface PatientDb extends JpaRepository<Patient, UUID> {
    @Query("SELECT p FROM Patient p WHERE p.NIK = :nik")
    Patient findByNIK(String nik);
}