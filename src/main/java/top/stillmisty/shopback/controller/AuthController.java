package top.stillmisty.shopback.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "认证", description = "用户登录注册相关接口")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest loginRequest) {
        String jwt = authService.login(loginRequest.username(), loginRequest.password());
        return ResponseEntity.ok()
                .body(ApiResponse.success(new LoginResponse(jwt)));
    }

    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public ResponseEntity<ApiResponse<LoginResponse>> register(@RequestBody LoginRequest loginRequest) {
        String jwt = authService.register(loginRequest.username(), loginRequest.password());
        return ResponseEntity.ok()
                .body(ApiResponse.success(new LoginResponse(jwt)));
    }
}