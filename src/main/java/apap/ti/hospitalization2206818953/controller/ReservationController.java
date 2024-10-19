package apap.ti.hospitalization2206818953.controller;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import apap.ti.hospitalization2206818953.dto.request.AddPatientRequestDTO;
import apap.ti.hospitalization2206818953.dto.request.AddReservationRequestDTO;
import apap.ti.hospitalization2206818953.model.Nurse;
import apap.ti.hospitalization2206818953.model.Patient;
import apap.ti.hospitalization2206818953.model.Reservation;
import apap.ti.hospitalization2206818953.model.Room;
import apap.ti.hospitalization2206818953.service.NurseService;
import apap.ti.hospitalization2206818953.service.PatientService;
import apap.ti.hospitalization2206818953.service.ReservationService;
import apap.ti.hospitalization2206818953.service.RoomService;

@Controller
public class ReservationController {
    @Autowired
    private PatientService patientService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private NurseService nurseService;

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/reservations")
    public String viewAllReservations(Model model) {
        List<Reservation> reservationList = reservationService.getAllReservations();
        model.addAttribute("reservationList", reservationList);
        return "viewall-reservation";
    }

    @GetMapping("/reservations/create")
    public String createReservationForm(@RequestParam(value = "exist", required = false) boolean exist,
                                        @RequestParam(value = "nik", required = false) String nik, 
                                        @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                        @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                                        @RequestParam(value = "nurseId", required = false) UUID nurseId,
                                        Model model) {
        if (exist) {
            var reservationDTO = new AddReservationRequestDTO();
            List<Nurse> nurseList = nurseService.getAllNurses();
            model.addAttribute("reservationDTO", reservationDTO);
            model.addAttribute("nurseList", nurseList);
            if (startDate != null && endDate != null && !startDate.after(endDate)) {
                List<Room> availableRooms = roomService.getAvailableRooms(startDate, endDate);
                model.addAttribute("roomList", availableRooms);
                reservationDTO.setStartDate(startDate);
                reservationDTO.setEndDate(endDate);
                reservationDTO.setNurseId(nurseId);
            }
            return "form-create-reservation";
        } else if (!exist && nik != null) {
            var patientDTO = new AddPatientRequestDTO();
            model.addAttribute("patientDTO", patientDTO);
            return "form-create-patient";
        }
        return "form-nik-reservation";
    }

    @PostMapping("/reservations/create")
    public String postCreateReservationForm(@RequestParam(value = "exist", required = false) boolean exist,
                                            @RequestParam(value = "nik", required = false) String nik,
                                            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                                            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate,
                                            @ModelAttribute("patientDTO") AddPatientRequestDTO patientDTO,
                                            @ModelAttribute("reservationRequest") AddReservationRequestDTO reservationDTO, Model model) {
        if (startDate != null && endDate != null && !startDate.after(endDate)) {
            List<Room> roomList = roomService.getAvailableRooms(startDate, endDate);
            model.addAttribute("roomList", roomList);
        }
        if (nik != null) {
            Patient patient = patientService.getPatientByNIK(nik);
            if (patient != null) {
                model.addAttribute("patient", patient);
                return "patient-found";
            } else {
                model.addAttribute("nik", nik);
                return "patient-not-found";
            }
        } else if (reservationDTO != null) {
            var reservation = new Reservation();
            reservation.setDateIn(reservationDTO.getStartDate());
            reservation.setDateOut(reservationDTO.getEndDate());
            reservation.setPatient(patientService.getPatientById(reservationDTO.getPatientId()));
            reservation.setAssignedNurse(nurseService.getNurseById(reservationDTO.getNurseId()));
            reservation.setRoom(roomService.getRoomById(reservationDTO.getRoomId()));
            reservation.setCreatedAt(new Date());
            reservation.setUpdatedAt(new Date());
            reservationService.addReservation(reservation);

            model.addAttribute("message", "Reservation created successfully");
            return "response";
        } else {
            var patient = new Patient();
            patient.setName(patientDTO.getName());
            patient.setEmail(patientDTO.getEmail());
            patient.setNIK(patientDTO.getNIK());
            patient.setBirthDate(patientDTO.getBirthDate());
            patient.setGender(patientDTO.isGender());
            patient.setCreatedAt(new Date());
            patient.setUpdatedAt(new Date());
            patientService.addPatient(patient);
            return "form-create-reservation";
        }
    }
}
