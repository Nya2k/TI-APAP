package apap.ti.hospitalization2206818953.restdto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddRoomRequestRestDTO {
    private String id;
    private String name;
    private double pricePerDay;
    private int maxCapacity;
    private String description;
}