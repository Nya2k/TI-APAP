package apap.ti.hospitalization2206818953.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apap.ti.hospitalization2206818953.model.Reservation;
import apap.ti.hospitalization2206818953.model.Room;
import apap.ti.hospitalization2206818953.repository.ReservationDb;
import apap.ti.hospitalization2206818953.repository.RoomDb;

@Service
public class RoomServiceImpl implements RoomService {
    
    @Autowired
    private RoomDb roomDb;

    @Autowired
    private ReservationDb reservationDb;

    @Override
    public Room addRoom(Room room) {
        return roomDb.save(room);
    }

    @Override
    public List<Room> getAllRoom() {
        return roomDb.findAll();
    }

    @Override
    public Room getRoomById(String idRoom) {
        for (Room room : getAllRoom()) {
            if (room.getId().equals(idRoom)) {
                return room;
            }
        }
        return null;
    }

    @Override
    public Room updateRoom(Room room) {
        Room existingRoom = getRoomById(room.getId());
        if (existingRoom != null) {
            existingRoom.setName(room.getName());
            existingRoom.setDescription(room.getDescription());
            existingRoom.setMaxCapacity(room.getMaxCapacity());
            existingRoom.setPricePerDay(room.getPricePerDay());
            roomDb.save(existingRoom);
            return existingRoom;
        }
        return null;
    }

    @Override
    public void deleteRoom(Room room) {
        room.setDeleted(true);
        roomDb.save(room);
    }

    @Override
    public int countRooms() {
        return (int) roomDb.count();
    }

    @Override
    public int countRoomsByIsDeleted() {
        return roomDb.countByIsDeletedFalse();
    }

    @Override
    public List<Reservation> findReservationsByRoomIdAndDate(String roomId, Date dateIn, Date dateOut) {
        return reservationDb.findByRoomIdAndDateInBetween(roomId, dateIn, dateOut);
    }

    @Override
    public List<Room> getAvailableRooms(Date dateIn, Date dateOut) {
        List<Room> rooms = getAllRoom();

        return rooms.stream().filter(room -> {
            int currentReservations = reservationDb.countReservationsInRange(room.getId(), dateIn, dateOut);
            return room.getMaxCapacity() > currentReservations;
        }).toList();
    }
}
