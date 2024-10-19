package apap.ti.hospitalization2206818953.restdto.response;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReservationResponseDTO {
    private String id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID patientId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID nurseId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String roomId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<FacilityResponseDTO> listFacility;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Jakarta")
    private Date dateIn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Jakarta")
    private Date dateOut;

    private double totalFee;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Jakarta")
    private Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone = "Asia/Jakarta")
    private Date updatedAt;
}
