package apap.ti.hospitalization2206818953.restdto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RoomResponseDTO {
    private String id;
    private String name;
    private double pricePerDay;
}
