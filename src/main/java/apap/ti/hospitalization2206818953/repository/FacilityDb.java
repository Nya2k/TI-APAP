package apap.ti.hospitalization2206818953.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import apap.ti.hospitalization2206818953.model.Facility;
import java.util.UUID;

public interface FacilityDb extends JpaRepository<Facility, UUID> {
}