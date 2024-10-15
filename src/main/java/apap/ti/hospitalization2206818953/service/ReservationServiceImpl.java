package apap.ti.hospitalization2206818953.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apap.ti.hospitalization2206818953.model.Reservation;
import apap.ti.hospitalization2206818953.repository.ReservationDb;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationDb reservationDb;

    @Override
    public Reservation addReservation(Reservation reservation) {
        return reservationDb.save(reservation);
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationDb.findAll();
    }

    @Override
    public Reservation getReservationById(String idReservation) {
        return reservationDb.findById(idReservation).orElse(null);
    }

    @Override
    public Reservation updateReservation(Reservation reservation) {
        Reservation existingReservation = getReservationById(reservation.getId());
        if (existingReservation != null) {
            existingReservation.setDateIn(reservation.getDateIn());
            existingReservation.setDateOut(reservation.getDateOut());
            existingReservation.setTotalFee(reservation.getTotalFee());
            existingReservation.setPatient(reservation.getPatient());
            existingReservation.setAssignedNurse(reservation.getAssignedNurse());
            existingReservation.setRoom(reservation.getRoom());
            existingReservation.setListFacility(reservation.getListFacility());
            reservationDb.save(existingReservation);
            return existingReservation;
        }
        return null;
    }

    @Override
    public void deleteReservation(Reservation reservation) {
        reservation.setDeleted(true);
        reservationDb.save(reservation);
    }

    @Override
    public int countReservations() {
        return (int) reservationDb.count();
    }
}
