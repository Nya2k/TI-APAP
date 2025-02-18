package apap.ti.hospitalization2206818953.controller;

import java.text.DecimalFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
import apap.ti.hospitalization2206818953.dto.request.UpdateReservationRequestDTO;
import apap.ti.hospitalization2206818953.model.Facility;
import apap.ti.hospitalization2206818953.model.Nurse;
import apap.ti.hospitalization2206818953.model.Patient;
import apap.ti.hospitalization2206818953.model.Reservation;
import apap.ti.hospitalization2206818953.model.Room;
import apap.ti.hospitalization2206818953.service.FacilityService;
import apap.ti.hospitalization2206818953.service.NurseService;
import apap.ti.hospitalization2206818953.service.PatientService;
import apap.ti.hospitalization2206818953.service.ReservationService;
import apap.ti.hospitalization2206818953.service.RoomService;

@Controller
public class ReservationController {
    private final PatientService patientService;
    private final RoomService roomService;
    private final NurseService nurseService;
    private final ReservationService reservationService;
    private final FacilityService facilityService;

    public ReservationController(PatientService patientService,
                                RoomService roomService,
                                NurseService nurseService,
                                ReservationService reservationService,
                                FacilityService facilityService) {
        this.patientService = patientService;
        this.roomService = roomService;
        this.nurseService = nurseService;
        this.reservationService = reservationService;
        this.facilityService = facilityService;
    }

    @GetMapping("/reservations")
    public String listRestReservations(Model model) {
        try {
            var listReservation = reservationService.getAllReservationsFromRest();
            model.addAttribute("listReservation", listReservation);
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
            model.addAttribute("nik", nik);
            reservationDTO.setPatientNIK(nik);
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
                                            @RequestParam(value = "roomId", required = false) String roomId,
                                            @ModelAttribute("patientDTO") AddPatientRequestDTO patientDTO,
                                            @ModelAttribute("reservationRequest") AddReservationRequestDTO reservationDTO, Model model) {
        if (dateIn != null && dateOut != null && !dateIn.after(dateOut)) {
            List<Room> roomList = roomService.getAvailableRooms(dateIn, dateOut);
            model.addAttribute("roomList", roomList);
        }
        if (reservationDTO != null && reservationDTO.getDateOut() != null) {
            var reservation = new Reservation();
            reservation.setId(generateReservationId(reservationDTO.getDateIn(), reservationDTO.getDateOut(), nik));
            reservation.setDateIn(reservationDTO.getDateIn());
            reservation.setDateOut(reservationDTO.getDateOut());
            reservation.setRoom(roomService.getRoomById(reservationDTO.getRoomId()));
            reservation.setAssignedNurse(nurseService.getNurseById(reservationDTO.getNurseId()));
            reservation.setPatient(patientService.getPatientByNIK(nik));
            
            reservation.setTotalFee(0);
            reservation.setCreatedAt(new Date());
            reservation.setUpdatedAt(new Date());
            reservationService.addReservation(reservation);

            List<Facility> availableFacilities = facilityService.getAllFacilities();
            model.addAttribute("availableFacilities", availableFacilities);
            model.addAttribute("reservation", reservation);
            return "form-create-reservation-2";
        } else if (nik != null) {
            Patient patient = patientService.getPatientByNIK(nik);
            if (patient != null) {
                model.addAttribute("patient", patient);
                return "patient-found";
            } else {
                model.addAttribute("nik", nik);
                return "patient-not-found";
            }
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

            var reservationDTO2 = new AddReservationRequestDTO();
            List<Nurse> nurseList = nurseService.getAllNurses();
            model.addAttribute("reservationDTO", reservationDTO2);
            model.addAttribute("nurseList", nurseList);
            reservationDTO2.setPatientNIK(patientDTO.getNIK());
            return "form-create-reservation";
        }
    }

    @PostMapping("/reservations/save")
    public String addFacilities(@ModelAttribute Reservation reservation,
                                @RequestParam("facilityIds") List<UUID> facilityIds,
                                Model model) {
        Reservation existingReservation = reservationService.getReservationById(reservation.getId());
        
        if (existingReservation == null) {
            model.addAttribute("message", "Reservation not found!");
            return "error";
        }

        List<Facility> selectedFacilities = new ArrayList<>();
        double additionalFee = 0.0;

        for (UUID facilityId : facilityIds) {
            Facility facility = facilityService.getFacilityById(facilityId);
            if (facility != null) {
                selectedFacilities.add(facility);
                additionalFee += facility.getFee();
            }
        }

        if (existingReservation.getListFacility() == null) {
            existingReservation.setListFacility(new ArrayList<>());
        }
        existingReservation.getListFacility().addAll(selectedFacilities);
        double currentFee = existingReservation.getTotalFee();
        existingReservation.setTotalFee(currentFee + additionalFee);
        reservationService.updateReservation(existingReservation);
        model.addAttribute("message", "Reservasi berhasil disimpan");
        return "response";
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
        System.out.println(reservationId);
        
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

    @GetMapping("/reservations/{reservationId}/update-room")
    public String updateReservationForm(@PathVariable String reservationId, Model model) {
        Reservation reservation = reservationService.getReservationById(reservationId);

        UpdateReservationRequestDTO reservationDTO = new UpdateReservationRequestDTO();
        reservationDTO.setNurseId(reservation.getAssignedNurse().getId());
        reservationDTO.setDateIn(reservation.getDateIn());
        reservationDTO.setDateOut(reservation.getDateOut());
        reservationDTO.setRoomId(reservation.getRoom().getId());

        model.addAttribute("reservationDTO", reservationDTO);
        model.addAttribute("nurseList", nurseService.getAllNurses());
        model.addAttribute("roomList", roomService.getAvailableRooms(reservation.getDateIn(), reservation.getDateOut()));
        model.addAttribute("reservationId", reservationId);

        return "form-update-reservation";
    }

    @PostMapping("/reservations/{reservationId}/update-room")
    public String updateReservation(@PathVariable String reservationId,
                                    @ModelAttribute UpdateReservationRequestDTO reservationDTO,
                                    Model model) {
        Reservation existingReservation = reservationService.getReservationById(reservationId);

        existingReservation.setAssignedNurse(nurseService.getNurseById(reservationDTO.getNurseId()));
        existingReservation.setDateIn(reservationDTO.getDateIn());
        existingReservation.setDateOut(reservationDTO.getDateOut());
        Room newRoom = roomService.getRoomById(reservationDTO.getRoomId());
        existingReservation.setRoom(newRoom);

        long daysBetween = ChronoUnit.DAYS.between(
            reservationDTO.getDateIn().toInstant(),
            reservationDTO.getDateOut().toInstant()
        );
        double roomFee = newRoom.getPricePerDay() * daysBetween;
        double facilityFee = existingReservation.getListFacility().stream()
                                .mapToDouble(Facility::getFee)
                                .sum();
        existingReservation.setTotalFee(roomFee + facilityFee);

        existingReservation.setUpdatedAt(new Date());
        reservationService.updateReservation(existingReservation);

        model.addAttribute("message", "Reservasi berhasil di-update");
        return "response";
    }

    @GetMapping("/reservations/{reservationId}/update-facilities")
    public String updateFacilitiesForm(@PathVariable String reservationId, Model model) {
        Reservation reservation = reservationService.getReservationById(reservationId);

        List<Facility> availableFacilities = facilityService.getAllFacilities();
        model.addAttribute("reservation", reservation);
        model.addAttribute("availableFacilities", availableFacilities);
        model.addAttribute("selectedFacilities", reservation.getListFacility());

        return "form-update-facility";
    }

    @PostMapping("/reservations/{reservationId}/update-facilities")
    public String updateFacilities(@PathVariable String reservationId,
                                    @RequestParam("facilityIds") List<UUID> facilityIds,
                                    Model model) {
        Reservation existingReservation = reservationService.getReservationById(reservationId);

        List<Facility> selectedFacilities = new ArrayList<>();
        double additionalFee = 0.0;

        for (UUID facilityId : facilityIds) {
            Facility facility = facilityService.getFacilityById(facilityId);
            if (facility != null) {
                selectedFacilities.add(facility);
                additionalFee += facility.getFee();
            }
        }

        if (existingReservation.getListFacility() == null) {
            existingReservation.setListFacility(new ArrayList<>());
        }
        existingReservation.getListFacility().addAll(selectedFacilities);
        double currentFee = existingReservation.getTotalFee();
        existingReservation.setTotalFee(currentFee + additionalFee);
        existingReservation.setUpdatedAt(new Date());
        reservationService.updateReservation(existingReservation);

        model.addAttribute("message", "Fasilitas berhasil di-update");
        return "response";
    }
}
