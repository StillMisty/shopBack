package top.stillmisty.shopback.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "carousel")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
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

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Carousel carousel = (Carousel) o;
        return getId() != null && Objects.equals(getId(), carousel.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}