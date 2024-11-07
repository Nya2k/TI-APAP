package apap.ti.hospitalization2206818953.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddRoomRequestDTO {
    private String name;
    private String description;
    private int maxCapacity;
    private double pricePerDay;
    private boolean isDeleted;
}