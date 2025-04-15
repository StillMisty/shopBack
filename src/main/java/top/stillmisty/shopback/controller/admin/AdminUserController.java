package top.stillmisty.shopback.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.stillmisty.shopback.dto.AdminPasswordResetRequest;
import top.stillmisty.shopback.dto.NicknameChangeRequest;
import top.stillmisty.shopback.dto.UserIsAdminChangeRequest;
import top.stillmisty.shopback.dto.UserStatusChangeRequest;
import top.stillmisty.shopback.entity.Users;
import top.stillmisty.shopback.enums.UserStatus;
import top.stillmisty.shopback.service.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/users")
@Tag(name = "管理员-用户管理接口")
@PreAuthorize("hasRole('ADMIN')")
@Validated
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "获取所有用户列表")
    public ResponseEntity<Page<Users>> getAllUsers(
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") @Min(0)
            int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Min(1) @Max(100)
            int size
    ) {
        return ResponseEntity.ok(userService.getAllUsers(page, size));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "按状态筛选用户")
    public ResponseEntity<Page<Users>> getUsersByStatus(
            @PathVariable UserStatus status,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") @Min(0)
            int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Min(1) @Max(100)
            int size
    ) {
        return ResponseEntity.ok(userService.getUsersByStatus(status, page, size));
    }

    @GetMapping("/search")
    @Operation(summary = "搜索用户")
    public ResponseEntity<Page<Users>> searchUsers(
            @RequestParam String username,
            @Parameter(description = "页码") @RequestParam(defaultValue = "0") @Min(0)
            int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") @Min(1) @Max(100)
            int size
    ) {
        return ResponseEntity.ok(userService.searchUsers(username, page, size));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "获取用户详情")
    public ResponseEntity<Users> getUserById(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.info(userId));
    }

    @PutMapping("/{userId}/status")
    @Operation(summary = "修改用户状态")
    public ResponseEntity<Users> updateUserStatus(
            @PathVariable UUID userId,
            @RequestBody UserStatusChangeRequest userStatusChangeRequest
    ) {
        return ResponseEntity.ok(userService.updateUserStatus(userId, userStatusChangeRequest.status()));
    }

    @PutMapping("/{userId}/admin")
    @Operation(summary = "设置/取消管理员权限")
    public ResponseEntity<Users> setAdminRole(
            @PathVariable UUID userId,
            @RequestBody UserIsAdminChangeRequest userIsAdminChangeRequest
    ) {
        return ResponseEntity.ok(userService.setAdminRole(userId, userIsAdminChangeRequest.isAdmin()));
    }

    @PutMapping("/{userId}/nickname")
    @Operation(summary = "修改用户昵称")
    public ResponseEntity<Users> updateNickname(
            @PathVariable UUID userId,
            @RequestBody NicknameChangeRequest nicknameChangeRequest
    ) {
        return ResponseEntity.ok(userService.changeNickname(userId, nicknameChangeRequest.nickname()));
    }

    @PutMapping("/{userId}/password")
    @Operation(summary = "重置用户密码")
    public ResponseEntity<Users> resetPassword(
            @PathVariable
            UUID userId,
            @RequestBody
            AdminPasswordResetRequest adminPasswordResetRequest
    ) {
        return ResponseEntity.ok(userService.resetUserPassword(userId, adminPasswordResetRequest.newPassword()));
    }
}