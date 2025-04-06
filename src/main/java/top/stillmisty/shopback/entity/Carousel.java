package top.stillmisty.shopback.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "carousel")
@Data
public class Carousel {
    @Id
    @GeneratedValue
    @Schema(description = "轮播图 ID")
    private UUID id;

    @Schema(description = "轮播图图片URL")
    private String imageUrl;

    @Schema(description = "轮播图跳转链接")
    @Column(nullable = false)
    private String linkUrl;

    @Schema(description = "轮播图标题")
    @Column(nullable = false)
    private String title;

    @Schema(description = "轮播图排序号")
    @Column(nullable = false)
    private Integer sortOrder;

    @Schema(description = "轮播图是否启用")
    @Column(nullable = false)
    private Boolean enabled = true;
}