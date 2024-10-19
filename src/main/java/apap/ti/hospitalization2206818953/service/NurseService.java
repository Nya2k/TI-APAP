package apap.ti.hospitalization2206818953.service;

import java.util.List;
import java.util.UUID;

import apap.ti.hospitalization2206818953.model.Nurse;

public interface NurseService {
    Nurse addNurse(Nurse nurse);
    List<Nurse> getAllNurses();
    Nurse getNurseById(UUID idNurse);
    Nurse updateNurse(Nurse nurse);
    void deleteNurse(Nurse nurse);
    int countNurses();
}
