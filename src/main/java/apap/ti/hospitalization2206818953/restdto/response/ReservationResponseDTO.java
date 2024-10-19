package apap.ti.hospitalization2206818953.restdto.response;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
    private NurseResponseDTO nurse;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private RoomResponseDTO room;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private PatientResponseDTO patient;

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

    private boolean isDeleted;

    public String getStatus() {
        Date now = new Date();

        if (now.after(dateOut)) {
            return "Done";
        } else if (now.before(dateIn)) {
            return "Upcoming";
        } else {
            return "Ongoing";
        }
    }

    public double getFinalFee() {
        double finalFee = totalFee;

        for (FacilityResponseDTO facility : listFacility) {
            finalFee += facility.getFee();
        }

        return finalFee;
    }

    public long getTotalDays() {
        long diff = dateOut.getTime() - dateIn.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }
}
