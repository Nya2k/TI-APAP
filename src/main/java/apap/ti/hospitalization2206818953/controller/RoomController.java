package apap.ti.hospitalization2206818953.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import apap.ti.hospitalization2206818953.dto.request.AddRoomRequestDTO;
import apap.ti.hospitalization2206818953.dto.request.UpdateRoomRequestDTO;
import apap.ti.hospitalization2206818953.model.Reservation;
import apap.ti.hospitalization2206818953.model.Room;
import apap.ti.hospitalization2206818953.service.RoomService;
import jakarta.validation.Valid;

@Controller
public class RoomController {
    @Autowired
    private RoomService roomService;
    
    @GetMapping("/rooms")
    public String listRooms(Model model) {
        model.addAttribute("rooms", roomService.getAllRoom());
        return "viewall-room";
    }

    @GetMapping("/rooms/{roomId}")
    public String getRoomDetails(@PathVariable String roomId, 
                                @RequestParam(required = false) String dateIn,
                                @RequestParam(required = false) String dateOut,
                                Model model) throws ParseException {

        Room room = roomService.getRoomById(roomId);
        model.addAttribute("room", room);
        
        Date today = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        if (dateIn == null || dateOut == null) {
            dateIn = formatter.format(today);
            dateOut = formatter.format(today);
        }

        Date startDate = formatter.parse(dateIn);
        Date endDate = formatter.parse(dateOut);

        List<Reservation> reservations = roomService.findReservationsByRoomIdAndDate(roomId, startDate, endDate);

        model.addAttribute("dateIn", dateIn);
        model.addAttribute("dateOut", dateOut);
        if (dateIn == null || dateOut == null) {
            model.addAttribute("listReservation", room.getListReservation());
        }
        model.addAttribute("listReservation", reservations);
        model.addAttribute("availableQuota", room.getAvailableQuota());

        return "view-room";
    }


    @PostMapping("/rooms/{roomId}/delete")
    public String deleteRoom(@PathVariable String roomId, Model model) {
        Room room = roomService.getRoomById(roomId);
        roomService.deleteRoom(room);
        model.addAttribute("message", "Berhasil menghapus ruangan " + roomId);
        return "response";
    }

    @GetMapping("/rooms/create")
    public String formAddRoom(Model model) {
        var roomDTO = new AddRoomRequestDTO();
        
        model.addAttribute("roomDTO", roomDTO);

        return "form-add-room";
    }

    @PostMapping("/rooms/create")
    public String addRoom(@ModelAttribute("roomDTO") AddRoomRequestDTO roomDTO, Model model) {
        var room = new Room();

        String roomId = generateRoomId();
        room.setId(roomId);
        room.setName(roomDTO.getName());
        room.setMaxCapacity(roomDTO.getMaxCapacity());
        room.setPricePerDay(roomDTO.getPricePerDay());
        room.setDescription(roomDTO.getDescription());
        room.setCreatedAt(new Date());
        room.setUpdatedAt(new Date());
        roomService.addRoom(room);

        model.addAttribute("message", String.format("Ruangan %s dengan ID %s berhasil ditambahkan.", room.getName(), room.getId()));

        return "response";
    }

    private String generateRoomId() {
        int n = roomService.countRooms();
        return String.format("RM%04d", n + 1);
    }

    @GetMapping("/rooms/{roomId}/update")
    public String updateRoomForm(@PathVariable String roomId, Model model) {
        Room room = roomService.getRoomById(roomId);
        model.addAttribute("roomDTO", room);
        
        return "form-update-room";
    }

    @PostMapping("/rooms/{roomId}/update")
    public String updateRoom(@PathVariable String roomId,
                                @Valid @ModelAttribute UpdateRoomRequestDTO roomDTO,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("message", "Gagal mengupdate ruangan, ada kesalahan di input.");
            return "response";
        }

        Room room = new Room();
        room.setId(roomId);
        room.setName(roomDTO.getName());
        room.setMaxCapacity(roomDTO.getMaxCapacity());
        room.setPricePerDay(roomDTO.getPricePerDay());
        room.setDescription(roomDTO.getDescription());
        room.setUpdatedAt(new Date());
        roomService.updateRoom(room);

        model.addAttribute("message", String.format("Ruangan %s berhasil diupdate.", roomDTO.getName()));
        return "response";
    }
}
