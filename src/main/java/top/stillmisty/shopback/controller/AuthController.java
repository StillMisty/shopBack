package top.stillmisty.shopback.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.stillmisty.shopback.dto.ApiResponse;
import top.stillmisty.shopback.dto.LoginRequest;
import top.stillmisty.shopback.dto.LoginResponse;
import top.stillmisty.shopback.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        String jwt = authService.login(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.ok()
                .body(ApiResponse.success(new LoginResponse(jwt)));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<LoginResponse>> register(@RequestBody LoginRequest loginRequest) {
        String jwt = authService.register(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.ok()
                .body(ApiResponse.success(new LoginResponse(jwt)));
    }
}