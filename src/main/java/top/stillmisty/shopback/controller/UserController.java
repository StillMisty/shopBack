package top.stillmisty.shopback.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.stillmisty.shopback.dto.ApiResponse;
import top.stillmisty.shopback.dto.NicknameChangeRequest;
import top.stillmisty.shopback.dto.PasswordChangeRequest;
import top.stillmisty.shopback.entity.Users;
import top.stillmisty.shopback.service.UserService;
import top.stillmisty.shopback.utils.AuthUtils;

import java.io.IOException;
import java.util.UUID;


@RestController
@RequestMapping("/api/user")
@Tag(name = "用户信息", description = "用户信息修改相关接口")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/info")
    @Operation(summary = "获取用户信息")
    public ResponseEntity<ApiResponse<Users>> getUserInfo() {
        // 从安全上下文中获取当前用户ID
        UUID userId = AuthUtils.getCurrentUserId();
        Users user = userService.info(userId);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    @PutMapping("/password")
    @Operation(summary = "修改密码")
    public ResponseEntity<ApiResponse<Users>> changePassword(
            @Valid @RequestBody PasswordChangeRequest password
    ) {
        // 从安全上下文中获取当前用户ID
        UUID userId = AuthUtils.getCurrentUserId();
        if (userService.changePassword(userId, password.password())) {
            // 密码修改成功，返回用户信息
            return ResponseEntity.ok(ApiResponse.success(userService.info(userId)));
        } else {
            // 密码修改失败，返回错误信息
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("密码修改失败"));
        }
    }

    @PutMapping("/nickname")
    @Operation(summary = "修改昵称")
    public ResponseEntity<ApiResponse<Users>> changeNickname(
            @Valid @RequestBody NicknameChangeRequest nicknameChangeRequest
    ) {
        // 从安全上下文中获取当前用户ID
        UUID userId = AuthUtils.getCurrentUserId();
        if (userService.changeNickname(userId, nicknameChangeRequest.nickname())) {
            // 昵称修改成功，返回用户信息
            return ResponseEntity.ok(ApiResponse.success(userService.info(userId)));
        } else {
            // 昵称修改失败，返回错误信息
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("昵称修改失败"));
        }
    }


    @PostMapping(path = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "修改头像")
    public ResponseEntity<ApiResponse<Users>> changeAvatar(
            @RequestParam("image") MultipartFile file
    ) {
        UUID userId = AuthUtils.getCurrentUserId();
        // 调用服务层方法处理文件上传
        try {
            if (userService.changeAvatar(userId, file)) {
                return ResponseEntity.ok(ApiResponse.success(userService.info(userId)));
            } else {
                // 头像修改失败，返回错误信息
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error("头像修改失败"));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
