package top.stillmisty.shopback.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import top.stillmisty.shopback.security.CustomUserDetails;

import java.util.UUID;

public class AuthUtils {
    public static UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        if (customUserDetails.isAdmin()) {
            throw new RuntimeException("当前用户是管理员，无法获取用户ID");
        }
        return customUserDetails.getId();
    }

    public static UUID getCurrentAdminUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        if (customUserDetails.isAdmin()) {
            return customUserDetails.getId();
        } else {
            throw new RuntimeException("当前用户不是管理员，无法获取管理员ID");
        }
    }
}
