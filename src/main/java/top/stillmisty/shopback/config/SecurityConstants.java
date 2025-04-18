package top.stillmisty.shopback.config;

import java.util.List;

public class SecurityConstants {
    public static final List<String> WHITE_LIST_URLS = List.of(
            "/api/auth/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/api/carousel/**",
            "/api/product/**",
            "/public/**",
            "/api/search/**"
    );

    // 判断路径是否在白名单中的工具方法
    public static boolean isUrlInWhiteList(String path) {
        return WHITE_LIST_URLS.stream()
                .anyMatch(pattern -> {
                    if (pattern.endsWith("/**")) {
                        String prefix = pattern.substring(0, pattern.length() - 3);
                        return path.startsWith(prefix);
                    }
                    return path.equals(pattern);
                });
    }
}