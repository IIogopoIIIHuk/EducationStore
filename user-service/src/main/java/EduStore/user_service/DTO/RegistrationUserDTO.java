package EduStore.user_service.DTO;

import lombok.Data;

@Data
public class RegistrationUserDTO {
    private String username;
    private String email;
    private String password;
}
