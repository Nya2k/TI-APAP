package apap.ti.hospitalization2206818953.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateRoomRequestDTO extends AddRoomRequestDTO {
    private String id;
}
