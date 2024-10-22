package apap.ti.hospitalization2206818953.restservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apap.ti.hospitalization2206818953.model.Room;
import apap.ti.hospitalization2206818953.repository.RoomDb;
import apap.ti.hospitalization2206818953.restdto.request.AddRoomRequestRestDTO;
import apap.ti.hospitalization2206818953.restdto.response.RoomResponseDTO;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class RoomRestServiceImpl implements RoomRestService {
    @Autowired
    RoomDb roomDb;

    @Override
    public RoomResponseDTO addRoom(AddRoomRequestRestDTO roomDTO) {
        Room room = new Room();
        room.setId(roomDTO.getId());
        room.setName(roomDTO.getName());
        room.setMaxCapacity(roomDTO.getMaxCapacity());
        room.setPricePerDay(roomDTO.getPricePerDay());
        room.setDescription(roomDTO.getDescription());

        var newRoom = roomDb.save(room);
        return roomToRoomResponseDTO(newRoom);
    }

    private RoomResponseDTO roomToRoomResponseDTO(Room room) {
        var roomResponseDTO = new RoomResponseDTO();
        roomResponseDTO.setId(room.getId());
        roomResponseDTO.setName(room.getName());
        roomResponseDTO.setPricePerDay(room.getPricePerDay());
        roomResponseDTO.setMaxCapacity(room.getMaxCapacity());
        roomResponseDTO.setDescription(room.getDescription());

        return roomResponseDTO;
    }
}
