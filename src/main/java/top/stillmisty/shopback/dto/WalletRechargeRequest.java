package top.stillmisty.shopback.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

import java.math.BigDecimal;

@Schema(description = "钱包充值请求")
public record WalletRechargeRequest(
        @Schema(description = "充值金额", example = "100.00")
        @Min(0)
        BigDecimal amount
) {
}
