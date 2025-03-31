package top.stillmisty.shopback.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.stillmisty.shopback.entity.Users;
import top.stillmisty.shopback.service.UserService;


@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/info")
    public ResponseEntity<Users> getUserInfo() {
        // 从安全上下文中获取当前用户ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        Long userId = Long.valueOf(userDetails.getUsername());
        Users user = userService.info(userId);
        return ResponseEntity.ok(user);
    }
}
