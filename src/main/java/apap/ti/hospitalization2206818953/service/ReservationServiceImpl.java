package apap.ti.hospitalization2206818953.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import apap.ti.hospitalization2206818953.model.Reservation;
import apap.ti.hospitalization2206818953.repository.ReservationDb;
import apap.ti.hospitalization2206818953.restdto.response.BaseResponseDTO;
import apap.ti.hospitalization2206818953.restdto.response.ReservationResponseDTO;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationDb reservationDb;

    private final WebClient webClient;

        public ReservationServiceImpl(WebClient.Builder webClientBuilder) {
            this.webClient = webClientBuilder
                            .baseUrl("http://localhost:8080/api")
                            .build();
        }

    @Override
    public Reservation addReservation(Reservation reservation) {
        return reservationDb.save(reservation);
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationDb.findAll();
    }

    @Override
    public List<ReservationResponseDTO> getAllReservationsFromRest() throws Exception {
        var response = webClient
                .get()
                .uri("/reservations")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<List<ReservationResponseDTO>>>() {})
                .block();

        if (response == null) {
            throw new Exception("Failed to consume API getAllReservations");
        }

        if (response.getStatus() != 200) {
            throw new Exception(response.getMessage());
        }

        return response.getData();
    }

    @Override
    public Reservation getReservationById(String idReservation) {
        return reservationDb.findById(idReservation).orElse(null);
    }

    @Override
    public ReservationResponseDTO getReservationByIdFromRest(String idReservation) throws Exception {
        var response = webClient
                .get()
                .uri("/reservations/" + idReservation)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<ReservationResponseDTO>>() {})
                .block();

        if (response == null) {
            throw new Exception("Failed to consume API getReservationById");
        }

        if (response.getStatus() != 200) {
            throw new Exception(response.getMessage());
        }

        return response.getData();
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
