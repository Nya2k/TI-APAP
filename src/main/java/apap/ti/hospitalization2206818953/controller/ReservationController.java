package apap.ti.hospitalization2206818953.controller;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import apap.ti.hospitalization2206818953.dto.request.AddPatientRequestDTO;
import apap.ti.hospitalization2206818953.dto.request.AddReservationRequestDTO;
import apap.ti.hospitalization2206818953.model.Nurse;
import apap.ti.hospitalization2206818953.model.Patient;
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

    // @GetMapping("/reservations")
    // public String viewAllReservations(Model model) {
    //     List<Reservation> reservationList = reservationService.getAllReservations();
    //     model.addAttribute("reservationList", reservationList);
    //     return "viewall-reservation";
    // }

    @GetMapping("/reservations")
    public String listRestReservations(Model model) {
        try {
            var listReservations = reservationService.getAllReservationsFromRest();
            model.addAttribute("listReservations", listReservations);
            return "viewall-reservation";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "response";
        }
    }

    @GetMapping("/reservations/{id}")
    public String viewReservationDetail(@PathVariable("id") String id, Model model) {
        try {
            var reservation = reservationService.getReservationByIdFromRest(id);
            DecimalFormat df = new DecimalFormat("#,###");
            model.addAttribute("formattedTotalFee", df.format(reservation.getTotalFee()));
            model.addAttribute("reservation", reservation);
            return "view-reservation";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "response";
        }
    }

    @GetMapping("/reservations/create")
    public String createReservationForm(@RequestParam(value = "exist", required = false) boolean exist,
                                        @RequestParam(value = "nik", required = false) String nik, 
                                        @RequestParam(value = "dateIn", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateIn,
                                        @RequestParam(value = "dateOut", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateOut,
                                        @RequestParam(value = "nurseId", required = false) UUID nurseId,
                                        Model model) {
        if (exist) {
            var reservationDTO = new AddReservationRequestDTO();
            List<Nurse> nurseList = nurseService.getAllNurses();
            model.addAttribute("reservationDTO", reservationDTO);
            model.addAttribute("nurseList", nurseList);
            if (dateIn != null && dateOut != null && !dateIn.after(dateOut)) {
                List<Room> availableRooms = roomService.getAvailableRooms(dateIn, dateOut);
                model.addAttribute("roomList", availableRooms);
                reservationDTO.setDateIn(dateIn);
                reservationDTO.setDateOut(dateOut);
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
                                            @RequestParam(value = "dateIn", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateIn,
                                            @RequestParam(value = "dateOut", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateOut,
                                            @ModelAttribute("patientDTO") AddPatientRequestDTO patientDTO,
                                            @ModelAttribute("reservationRequest") AddReservationRequestDTO reservationDTO, Model model) {
        if (dateIn != null && dateOut != null && !dateIn.after(dateOut)) {
            List<Room> roomList = roomService.getAvailableRooms(dateIn, dateOut);
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
            // var reservation = new Reservation();
            // reservation.setId(generateReservationId(reservationDTO.getDateIn(), reservationDTO.getDateOut(), patientDTO.getNIK()));
            // reservation.setDateIn(reservationDTO.getDateIn());
            // reservation.setDateOut(reservationDTO.getDateOut());
            // reservation.setPatient(patientService.getPatientById(reservationDTO.getPatientId()));
            // reservation.setAssignedNurse(nurseService.getNurseById(reservationDTO.getNurseId()));
            // reservation.setRoom(roomService.getRoomById(reservationDTO.getRoomId()));
            // reservation.setCreatedAt(new Date());
            // reservation.setUpdatedAt(new Date());
            // reservationService.addReservation(reservation);

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

    private String generateReservationId(Date dateIn, Date dateOut, String nik) {
        long differenceInMillis = dateOut.getTime() - dateIn.getTime();
        long differenceInDays = differenceInMillis / (1000 * 60 * 60 * 24);
        
        // 2 digit selisih
        String daysDiff = String.format("%02d", differenceInDays % 100);
        
        // hari buat reservasi
        Calendar calendar = Calendar.getInstance();
        String[] daysOfWeek = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
        String reservationDay = daysOfWeek[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        
        // 4 huruf terakhir NIK pasien
        String nikLastFour = nik.length() >= 4 ? nik.substring(nik.length() - 4) : String.format("%04d", 0);
        
        // jumlah reservasi
        int reservationCount = reservationService.countReservations();
        
        String reservationId = String.format("RES%s%s%s%s", daysDiff, reservationDay, nikLastFour, String.format("%04d", reservationCount + 1));
        
        return reservationId;
    }

    @PostMapping("/reservations/{id}/delete")
    public String deleteReservation(@PathVariable("id") String id, Model model) {
        try {
            var reservation = reservationService.getReservationById(id);
            reservationService.deleteReservation(reservation);
            model.addAttribute("message", "Berhasil menghapus reservasi " + id);
            return "response";
        } catch (Exception e) {
            model.addAttribute("message", "Gagal menghapus reservasi " + id);
            return "response";
        }
    }

    @GetMapping("/reservations/chart")
    public String showReservationChart(Model model) {
        String period = "monthly";
        int year = 2024;
        
        try {
            List<Integer> stats = reservationService.getReservationStatsFromRest(period, year);
            model.addAttribute("stats", stats);
            model.addAttribute("period", period);
            model.addAttribute("year", year);
            return "reservation-chart";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "response";
        }
    }

}
