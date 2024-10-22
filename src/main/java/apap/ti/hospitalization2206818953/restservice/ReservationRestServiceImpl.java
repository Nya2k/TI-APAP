package apap.ti.hospitalization2206818953.restservice;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import apap.ti.hospitalization2206818953.model.Reservation;
import apap.ti.hospitalization2206818953.repository.ReservationDb;
import apap.ti.hospitalization2206818953.restdto.response.FacilityResponseDTO;
import apap.ti.hospitalization2206818953.restdto.response.NurseResponseDTO;
import apap.ti.hospitalization2206818953.restdto.response.PatientResponseDTO;
import apap.ti.hospitalization2206818953.restdto.response.ReservationResponseDTO;
import apap.ti.hospitalization2206818953.restdto.response.RoomResponseDTO;

@Service
@Transactional
public class ReservationRestServiceImpl implements ReservationRestService {

    @Autowired
    ReservationDb reservationDb;

    @Override
    public List<ReservationResponseDTO> getAllReservations() {
        var listReservation = reservationDb.findAll();
        var listReservationResponseDTO = new ArrayList<ReservationResponseDTO>();

        listReservation.forEach(reservation -> {
            var reservationResponseDTO = reservationToReservationResponseDTO(reservation);
            listReservationResponseDTO.add(reservationResponseDTO);

        });

        return listReservationResponseDTO;
    }

    @Override
    public ReservationResponseDTO getReservationById(String idReservation) {
        var reservation = reservationDb.findById(idReservation).orElse(null);
        if (reservation == null) {
            return null;
        }
        return reservationToReservationResponseDTO(reservation);
    }

    private ReservationResponseDTO reservationToReservationResponseDTO(Reservation reservation) {
        var reservationResponseDTO = new ReservationResponseDTO();
        reservationResponseDTO.setId(reservation.getId());
        reservationResponseDTO.setDateIn(reservation.getDateIn());
        reservationResponseDTO.setDateOut(reservation.getDateOut());
        reservationResponseDTO.setTotalFee(reservation.getTotalFee());
        reservationResponseDTO.setCreatedAt(reservation.getCreatedAt());
        reservationResponseDTO.setUpdatedAt(reservation.getUpdatedAt());
        reservationResponseDTO.setDeleted(reservation.isDeleted());
        reservationResponseDTO.setTotalFee(reservation.getTotalFee());

        var patientResponseDTO = new PatientResponseDTO();
        patientResponseDTO.setId(reservation.getPatient().getId());
        patientResponseDTO.setName(reservation.getPatient().getName());
        patientResponseDTO.setNIK(reservation.getPatient().getNIK());
        patientResponseDTO.setEmail(reservation.getPatient().getEmail());
        patientResponseDTO.setGender(reservation.getPatient().isGender());
        reservationResponseDTO.setPatient(patientResponseDTO);

        var roomResponseDTO = new RoomResponseDTO();
        roomResponseDTO.setId(reservation.getRoom().getId());
        roomResponseDTO.setName(reservation.getRoom().getName());
        roomResponseDTO.setPricePerDay(reservation.getRoom().getPricePerDay());
        reservationResponseDTO.setRoom(roomResponseDTO);

        var nurseResponseDTO = new NurseResponseDTO();
        nurseResponseDTO.setName(reservation.getAssignedNurse().getName());
        reservationResponseDTO.setNurse(nurseResponseDTO);

        if (reservation.getListFacility() != null) {
            var listFacilityResponseDTO = new ArrayList<FacilityResponseDTO>();
            reservation.getListFacility().forEach(facility -> {
                var facilityResponseDTO = new FacilityResponseDTO();
                facilityResponseDTO.setId(facility.getId());
                facilityResponseDTO.setName(facility.getName());
                facilityResponseDTO.setFee(facility.getFee());
                facilityResponseDTO.setCreatedAt(facility.getCreatedAt());
                facilityResponseDTO.setUpdatedAt(facility.getUpdatedAt());
                listFacilityResponseDTO.add(facilityResponseDTO);
            });
            reservationResponseDTO.setListFacility(listFacilityResponseDTO);
        }

        return reservationResponseDTO;
    }


    @Override
    public List<Integer> getReservationStats(String period, int year) throws Exception {
        List<Reservation> reservations = reservationDb.findByYear(year);

        List<Integer> stats = new ArrayList<>();
        if ("monthly".equalsIgnoreCase(period)) {
            for (int i = 0; i < 12; i++) stats.add(0);
            for (Reservation reservation : reservations) {
                int month = reservation.getDateIn().toInstant().atZone(ZoneId.systemDefault()).getMonthValue() - 1;
                stats.set(month, stats.get(month) + 1);
            }
        } else if ("quarter".equalsIgnoreCase(period)) {
            for (int i = 0; i < 4; i++) stats.add(0);
            for (Reservation reservation : reservations) {
                int month = reservation.getDateIn().toInstant().atZone(ZoneId.systemDefault()).getMonthValue();
                int quarter = (month - 1) / 3;
                stats.set(quarter, stats.get(quarter) + 1);
            }
        } else {
            throw new Exception("Invalid period type");
        }

        return stats;
    }
}
