package top.stillmisty.shopback.dto;

import jakarta.validation.constraints.Size;


public record LoginRequest(
        @Size(min = 1, max = 20, message = "用户名长度必须在1-20之间")
        String username,
        @Size(min = 6, max = 20, message = "密码长度必须在6-20之间")
        String password
) {
}