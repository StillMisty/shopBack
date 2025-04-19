package top.stillmisty.shopback.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "添加类别请求")
public record CategoryAddRequest(
        @NotBlank(message = "类别名称不能为空")
        @Schema(description = "类别名称", example = "电子产品")
        String categoryName,

        @NotNull(message = "类别层级不能为空")
        @Schema(description = "类别层级，1为顶级类别，2为二级类别，3为三级类别", example = "1")
        Integer level,

        @Schema(description = "父类别ID，顶级类别可为null")
        UUID parentId,

        @Schema(description = "类别排序号", example = "1")
        Integer sortOrder
) {
}