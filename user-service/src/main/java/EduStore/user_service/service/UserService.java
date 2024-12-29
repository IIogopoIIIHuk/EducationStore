package EduStore.user_service.service;


import EduStore.user_service.entity.User;
import EduStore.user_service.DTO.RegistrationUserDTO;
import EduStore.user_service.repo.RoleRepository;
import EduStore.user_service.repo.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


}
