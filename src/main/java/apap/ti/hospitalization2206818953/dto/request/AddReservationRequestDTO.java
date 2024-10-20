package apap.ti.hospitalization2206818953.dto.request;

import java.util.Date;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddReservationRequestDTO {
    private UUID patientId;
    private UUID nurseId;
    private String roomId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateIn;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOut;
}