package top.stillmisty.shopback.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "categories")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Category {
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @Schema(description = "子类别")
    @ToString.Exclude
    @JsonIgnore
    private final Set<Category> children = new HashSet<>();

    @Id
    @GeneratedValue
    @Schema(description = "类别ID")
    private UUID categoryId;

    @Column(nullable = false, length = 100)
    @Schema(description = "类别名称")
    private String categoryName;

    @Schema(description = "类别层级，1为顶级类别，2为二级类别，3为三级类别")
    @Column(nullable = false)
    private Integer level;

    @ManyToOne()
    @JoinColumn(name = "parent_id")
    @Schema(description = "父级类别")
    @ToString.Exclude
    @JsonIgnore
    private Category parent;

    @Schema(description = "类别排序号")
    private Integer sortOrder;

    public Category(String categoryName, Integer level, Category parent) {
        this.categoryName = categoryName;
        this.level = level;
        this.parent = parent;
        this.sortOrder = 0;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Category category = (Category) o;
        return getCategoryId() != null && Objects.equals(getCategoryId(), category.getCategoryId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}