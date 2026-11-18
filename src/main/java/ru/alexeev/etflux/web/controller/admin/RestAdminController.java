package ru.alexeev.etflux.web.controller.admin;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.alexeev.etflux.web.controller.admin.dto.UserAdminDto;
import ru.alexeev.etflux.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@AllArgsConstructor
public class RestAdminController {
    private UserService userService;

    @GetMapping
    public List<UserAdminDto> findUsers(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size){
        return userService.findAll(page, size);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserAdminDto> findUser(@PathVariable Long userId){
        return ResponseEntity.ok(userService.findById(userId));
    }

    @GetMapping("/block/{userId}")
    public ResponseEntity<String> blockUser(@PathVariable Long userId){
        userService.blockUser(userId);
        return ResponseEntity.ok("User blocked successfully");
    }

    @GetMapping("/add-role/{userId}")
    public ResponseEntity<String> changeRole(@PathVariable Long userId, @RequestParam Integer roleId){
        userService.addRole(userId, roleId);
        return ResponseEntity.ok("Role added successfully");
    }

    @GetMapping("delete-role/{userId}")
    public ResponseEntity<String> deleteRole(@PathVariable Long userId, @RequestParam Integer roleId){
        userService.deleteRole(userId, roleId);
        return ResponseEntity.ok("Role deleted successfully");
    }

    @GetMapping("change-status/{userId}")
    public ResponseEntity<String> changeStatus(@PathVariable Long userId, @RequestParam String status){
        userService.changeStatus(userId, status);
        return ResponseEntity.ok("status changed successfully");
    }

}
