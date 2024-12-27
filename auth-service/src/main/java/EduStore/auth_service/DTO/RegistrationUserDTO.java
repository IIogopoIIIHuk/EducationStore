package EduStore.auth_service.DTO;

import lombok.Data;

@Data
public class RegistrationUserDTO {
    private String username;
    private String password;
    private String email;
}
