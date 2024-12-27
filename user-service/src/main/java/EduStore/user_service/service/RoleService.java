package EduStore.user_service.service;

import EduStore.user_service.entity.Role;
import EduStore.user_service.repo.RoleRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role getUserRole(){
        return roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new NoSuchElementException("Role not found in database"));
    }
}
