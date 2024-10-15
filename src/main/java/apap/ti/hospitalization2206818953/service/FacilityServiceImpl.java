package apap.ti.hospitalization2206818953.service;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apap.ti.hospitalization2206818953.model.Facility;
import apap.ti.hospitalization2206818953.repository.FacilityDb;

@Service
public class FacilityServiceImpl implements FacilityService {

    @Autowired
    private FacilityDb facilityDb;

    @Override
    public Facility addFacility(Facility facility) {
        return facilityDb.save(facility);
    }

    @Override
    public List<Facility> getAllFacilities() {
        return facilityDb.findAll();
    }

    @Override
    public Facility getFacilityById(UUID idFacility) {
        return facilityDb.findById(idFacility).orElse(null);
    }

    @Override
    public Facility updateFacility(Facility facility) {
        Facility existingFacility = getFacilityById(facility.getId());
        if (existingFacility != null) {
            existingFacility.setName(facility.getName());
            existingFacility.setFee(facility.getFee());
            facilityDb.save(existingFacility);
            return existingFacility;
        }
        return null;
    }

    @Override
    public void deleteFacility(Facility facility) {
        facility.setDeleted(true);
        facilityDb.save(facility);
    }

    @Override
    public int countFacilities() {
        return (int) facilityDb.count();
    }
}
