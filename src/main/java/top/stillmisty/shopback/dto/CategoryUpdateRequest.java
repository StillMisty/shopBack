package top.stillmisty.shopback.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "更新类别请求")
public record CategoryUpdateRequest(
        @NotNull(message = "类别ID不能为空")
        @Schema(description = "类别ID")
        UUID categoryId,

        @Schema(description = "类别名称", example = "电子产品")
        String categoryName,

        @Schema(description = "父类别ID")
        UUID parentId,

        @Schema(description = "类别描述", example = "包含各种电子设备")
        String description,

        @Schema(description = "类别排序号", example = "1")
        Integer sortOrder
) {
}