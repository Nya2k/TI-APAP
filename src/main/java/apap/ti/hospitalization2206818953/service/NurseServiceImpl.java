package apap.ti.hospitalization2206818953.service;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apap.ti.hospitalization2206818953.model.Nurse;
import apap.ti.hospitalization2206818953.repository.NurseDb;

@Service
public class NurseServiceImpl implements NurseService {

    @Autowired
    private NurseDb nurseDb;

    @Override
    public Nurse addNurse(Nurse nurse) {
        return nurseDb.save(nurse);
    }

    @Override
    public List<Nurse> getAllNurses() {
        return nurseDb.findAll();
    }

    @Override
    public Nurse getNurseById(UUID idNurse) {
        return nurseDb.findById(idNurse).orElse(null);
    }

    @Override
    public Nurse updateNurse(Nurse nurse) {
        Nurse existingNurse = getNurseById(nurse.getId());
        if (existingNurse != null) {
            existingNurse.setName(nurse.getName());
            existingNurse.setEmail(nurse.getEmail());
            existingNurse.setGender(nurse.isGender());
            nurseDb.save(existingNurse);
            return existingNurse;
        }
        return null;
    }

    @Override
    public void deleteNurse(Nurse nurse) {
        nurse.setDeleted(true);
        nurseDb.save(nurse);
    }

    @Override
    public int countNurses() {
        return (int) nurseDb.count();
    }
}
