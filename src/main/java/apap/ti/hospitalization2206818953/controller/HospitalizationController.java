package apap.ti.hospitalization2206818953.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import apap.ti.hospitalization2206818953.service.PatientService;
import apap.ti.hospitalization2206818953.service.ReservationService;
import apap.ti.hospitalization2206818953.service.RoomService;

@Controller
public class HospitalizationController {
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private PatientService patientService;
    
    @GetMapping(value = "/")
    public String home(Model model) {
        model.addAttribute("totalReservations", reservationService.countReservations());
        model.addAttribute("totalRooms", roomService.countRooms());
        model.addAttribute("totalPatients", patientService.countPatients());
        return "home.html";
    }
}