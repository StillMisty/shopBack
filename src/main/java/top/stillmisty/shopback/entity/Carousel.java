package top.stillmisty.shopback.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private String linkUrl;

    // 标题
    private String title;

    // 排序号
    private Integer sortOrder;

    // 是否启用
    private Boolean enabled = true;
}