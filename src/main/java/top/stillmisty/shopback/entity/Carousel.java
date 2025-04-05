package top.stillmisty.shopback.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "carousel")
@Data
public class Carousel {
    @Id
    @GeneratedValue
    private UUID id;

    // 图片URL
    private String imageUrl;

    // 跳转链接
    @Column(nullable = false)
    private String linkUrl;

    // 标题
    @Column(nullable = false)
    private String title;

    // 排序号
    @Column(nullable = false)
    private Integer sortOrder;

    // 是否启用
    @Column(nullable = false)
    private Boolean enabled = true;
}