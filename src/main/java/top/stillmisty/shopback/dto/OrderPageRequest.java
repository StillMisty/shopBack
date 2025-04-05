package top.stillmisty.shopback.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Sort;

@Schema(description = "订单分页列表请求")
public record OrderPageRequest(
        @Schema(description = "页码", example = "0")
        @Min(value = 0, message = "页码不能小于0")
        int page,

        @Schema(description = "每页大小", example = "10")
        @Min(value = 1, message = "每页大小不能小于1")
        @Max(value = 100, message = "每页大小不能大于100")
        int size,

        @Schema(description = "排序字段", example = "payTime")
        String sortBy,

        @Schema(description = "排序方向", example = "DESC")
        Sort.Direction sortDirection
) {
    public OrderPageRequest {
        if (sortBy == null) {
            sortBy = "payTime";
        }
        if (sortDirection == null) {
            sortDirection = Sort.Direction.DESC;
        }
    }
}
