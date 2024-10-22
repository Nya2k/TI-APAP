package apap.ti.hospitalization2206818953.restservice;

import apap.ti.hospitalization2206818953.restdto.request.AddRoomRequestRestDTO;
import apap.ti.hospitalization2206818953.restdto.response.RoomResponseDTO;

public interface RoomRestService {
    RoomResponseDTO addRoom(AddRoomRequestRestDTO roomDTO);
}
