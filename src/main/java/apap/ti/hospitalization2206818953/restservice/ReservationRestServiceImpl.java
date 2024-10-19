package apap.ti.hospitalization2206818953.restservice;

import apap.ti.hospitalization2206818953.restdto.response.ReservationResponseDTO;
import apap.ti.hospitalization2206818953.restdto.response.FacilityResponseDTO;
import apap.ti.hospitalization2206818953.model.Reservation;
import apap.ti.hospitalization2206818953.repository.ReservationDb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
}
