package apap.ti.hospitalization2206818953.restdto.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatientResponseDTO {
    private UUID id;
    private String NIK;
    private String name;
    private String email;
    private boolean gender;
}