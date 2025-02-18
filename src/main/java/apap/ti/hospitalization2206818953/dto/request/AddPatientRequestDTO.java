package apap.ti.hospitalization2206818953.dto.request;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddPatientRequestDTO {
    private String name;
    private boolean gender;
    private String NIK;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;
    private String email;
}
