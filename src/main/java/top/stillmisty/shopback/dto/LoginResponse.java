package top.stillmisty.shopback.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "登录响应")
public record LoginResponse(
        @Schema(description = "登陆所用 JWT", example = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1ZGVkNDM3My03NDUxLTQ3MWEtYTM1NC01ODM4NDQ0YTE2ZjAiLCJpYXQiOjE3NDM2NTQxNjgsImV4cCI6MTc0Mzc0MDU2OH0.TWA3y4Q2Jbw3iJ_xl94IKuHlYylXI_GJPitj0eQtv7U")
        String token
) {
}