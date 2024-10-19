package apap.ti.hospitalization2206818953.restcontroller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import apap.ti.hospitalization2206818953.restdto.response.BaseResponseDTO;
import apap.ti.hospitalization2206818953.restdto.response.ReservationResponseDTO;
import apap.ti.hospitalization2206818953.restservice.ReservationRestService;

@RestController
@RequestMapping("/api/reservations")
public class ReservationRestController {

    @Autowired
    ReservationRestService reservationRestService;

    @GetMapping("")
    public ResponseEntity<?> listReservations() {
        var baseResponseDTO = new BaseResponseDTO<List<ReservationResponseDTO>>();
        List<ReservationResponseDTO> listReservations = reservationRestService.getAllReservations();

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(listReservations);
        baseResponseDTO.setMessage("List reservations berhasil ditemukan");
        baseResponseDTO.setTimestamp(new Date());
        
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detailReservation(@PathVariable("id") String id) {
        var baseResponseDTO = new BaseResponseDTO<ReservationResponseDTO>();

        ReservationResponseDTO reservation = reservationRestService.getReservationById(id);
        if (reservation == null) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage("Data reservation tidak ditemukan");
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        }

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(reservation);
        baseResponseDTO.setMessage(String.format("Reservation dengan ID %s berhasil ditemukan", reservation.getId()));
        baseResponseDTO.setTimestamp(new Date());
        
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }
}
