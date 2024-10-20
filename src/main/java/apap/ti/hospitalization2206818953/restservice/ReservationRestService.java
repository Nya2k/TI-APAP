package apap.ti.hospitalization2206818953.restservice;

import apap.ti.hospitalization2206818953.restdto.response.ReservationResponseDTO;

import java.util.List;

public interface ReservationRestService {
    List<ReservationResponseDTO> getAllReservations();
    ReservationResponseDTO getReservationById(String idReservation);
    List<Integer> getReservationStats(String period, int year) throws Exception;
}