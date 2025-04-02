package top.stillmisty.shopback.dto;

import jakarta.validation.constraints.Size;


public record PasswordChangeRequest(
        @Size(min = 6, max = 20, message = "密码长度必须在6-20之间")
        String password
) {
}