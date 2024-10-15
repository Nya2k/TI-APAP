package apap.ti.hospitalization2206818953.service;

import java.util.UUID;
import java.util.List;
import apap.ti.hospitalization2206818953.model.Facility;

public interface FacilityService {
    Facility addFacility(Facility facility);
    List<Facility> getAllFacilities();
    Facility getFacilityById(UUID idFacility);
    Facility updateFacility(Facility facility);
    void deleteFacility(Facility facility);
    int countFacilities();
}
