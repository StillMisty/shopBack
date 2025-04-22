package top.stillmisty.shopback.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Address {
    @Id
    @GeneratedValue
    @Schema(description = "地址 ID")
    private Long addressId;

    @Schema(description = "是否默认地址")
    @Column(nullable = false)
    @JsonProperty("isDefault")
    private boolean isDefault;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    @ToString.Exclude
    private Users user;

    @Schema(description = "地址")
    private String address;

    @Schema(description = "收件人姓名")
    private String name;

    @Schema(description = "收件人电话")
    private String phone;

    public Address(String name, String address, String phone, Users user) {
        this.address = address;
        this.name = name;
        this.phone = phone;
        this.user = user;
        this.isDefault = false;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Address address = (Address) o;
        return getAddressId() != null && Objects.equals(getAddressId(), address.getAddressId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
