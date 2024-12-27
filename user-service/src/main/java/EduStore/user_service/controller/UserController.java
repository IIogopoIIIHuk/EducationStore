//package EduStore.user_service.controller;
//
//import EduStore.user_service.DTO.UserDTO;
//import EduStore.user_service.service.UserService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/users")
//@RequiredArgsConstructor
//public class UserController {
//
//    private final UserService userService;
//
//    @GetMapping("/{username}")
//    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username){
//        return userService.findByUsername(username)
//                .map(user -> ResponseEntity.ok(new UserDTO(user.getId(), user.getUsername(), user.getEmail())))
//                .orElse(ResponseEntity.notFound().build());
//    }
//}
