package apap.ti.hospitalization2206818953.service;

import java.util.List;
import apap.ti.hospitalization2206818953.model.Reservation;

public interface ReservationService {
    Reservation addReservation(Reservation reservation);
    List<Reservation> getAllReservations();
    Reservation getReservationById(String idReservation);
    Reservation updateReservation(Reservation reservation);
    void deleteReservation(Reservation reservation);
    int countReservations();
}