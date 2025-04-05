package top.stillmisty.shopback.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.stillmisty.shopback.dto.*;
import top.stillmisty.shopback.entity.Users;
import top.stillmisty.shopback.service.UserService;
import top.stillmisty.shopback.utils.AuthUtils;

import java.io.IOException;
import java.util.UUID;


@RestController
@RequestMapping("/api/users")
@Tag(name = "用户信息", description = "用户信息修改相关接口")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    @Operation(summary = "获取用户信息")
    public ResponseEntity<ApiResponse<UserResponse>> getUserInfo(
            @PathVariable("userId") UUID userId
    ) {
        Users user = userService.info(userId);
        UserResponse userResponse = new UserResponse(
                user.getUserId(),
                user.getNickname(),
                user.getAvatar(),
                user.getUserStatus()
        );
        return ResponseEntity.ok(ApiResponse.success(userResponse));
    }

    @GetMapping("/me")
    @Operation(summary = "获取当前用户信息")
    public ResponseEntity<ApiResponse<Users>> getUserInfo() {
        // 从安全上下文中获取当前用户ID
        UUID userId = AuthUtils.getCurrentUserId();
        Users user = userService.info(userId);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @PatchMapping("/me/password")
    @Operation(summary = "修改密码")
    public ResponseEntity<ApiResponse<Users>> changePassword(
            @Valid @RequestBody PasswordChangeRequest password
    ) {
        // 从安全上下文中获取当前用户ID
        UUID userId = AuthUtils.getCurrentUserId();
        Users users = userService.changePassword(userId, password.password());
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    @PatchMapping("/me/nickname")
    @Operation(summary = "修改昵称")
    public ResponseEntity<ApiResponse<Users>> changeNickname(
            @Valid @RequestBody NicknameChangeRequest nicknameChangeRequest
    ) {
        // 从安全上下文中获取当前用户ID
        UUID userId = AuthUtils.getCurrentUserId();
        Users users = userService.changeNickname(userId, nicknameChangeRequest.nickname());
        return ResponseEntity.ok(ApiResponse.success(users));
    }


    @PatchMapping(path = "/me/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "修改头像")
    public ResponseEntity<ApiResponse<Users>> changeAvatar(
            @RequestParam("image") MultipartFile file
    ) {
        UUID userId = AuthUtils.getCurrentUserId();
        // 调用服务层方法处理文件上传
        try {
            Users users = userService.changeAvatar(userId, file);
            return ResponseEntity.ok(ApiResponse.success(users));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PatchMapping("/me/wallet")
    @Operation(summary = "充值余额")
    public ResponseEntity<ApiResponse<Users>> rechargeWallet(
            @RequestBody WalletRechargeRequest walletRechargeRequest
    ) {
        UUID userId = AuthUtils.getCurrentUserId();
        // 调用服务层方法处理充值
        Users users = userService.rechargeWallet(userId, walletRechargeRequest.amount());
        return ResponseEntity.ok(ApiResponse.success(users));
    }
}
