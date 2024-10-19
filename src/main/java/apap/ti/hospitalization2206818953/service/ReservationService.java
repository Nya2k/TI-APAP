package apap.ti.hospitalization2206818953.service;

import java.util.List;

import apap.ti.hospitalization2206818953.model.Reservation;
import apap.ti.hospitalization2206818953.restdto.response.ReservationResponseDTO;

public interface ReservationService {
    Reservation addReservation(Reservation reservation);
    List<Reservation> getAllReservations();
    Reservation getReservationById(String idReservation);
    Reservation updateReservation(Reservation reservation);
    void deleteReservation(Reservation reservation);
    int countReservations();
    List<ReservationResponseDTO> getAllReservationsFromRest() throws Exception;
    ReservationResponseDTO getReservationByIdFromRest(String idReservation) throws Exception;
}