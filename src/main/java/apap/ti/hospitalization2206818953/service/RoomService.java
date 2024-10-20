package apap.ti.hospitalization2206818953.service;

import java.util.Date;
import java.util.List;

import apap.ti.hospitalization2206818953.model.Reservation;
import apap.ti.hospitalization2206818953.model.Room;

public interface RoomService {
    Room addRoom(Room room);
    List<Room> getAllRoom();
    Room getRoomById(String idRoom);
    Room updateRoom(Room room);
    void deleteRoom(Room room);
    int countRooms();
    int countRoomsByIsDeleted();
    List<Reservation> findReservationsByRoomIdAndDate(String roomId, Date dateIn, Date dateOut);
    List<Room> getAvailableRooms(Date dateIn, Date dateOut);
}