package top.stillmisty.shopback.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;
import top.stillmisty.shopback.config.InstantToTimestampSerializer;
import top.stillmisty.shopback.enums.UserStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "users")
@AllArgsConstructor
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Users {

    @Id
    @GeneratedValue
    @Schema(description = "用户 ID")
    private UUID userId;

    @Column(nullable = false, unique = true, length = 20)
    @Schema(description = "用户账号")
    private String username;

    @Column(nullable = false)
    @JsonIgnore  // 忽略密码的序列化
    @Schema(description = "用户密码")
    private String password;

    @Column(nullable = false, precision = 19, scale = 2)
    @Schema(description = "用户钱包余额")
    private BigDecimal wallet;

    @Column(nullable = false, length = 20)
    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "用户头像 URL")
    private String avatar;

    @Column(nullable = false)
    @JsonSerialize(using = InstantToTimestampSerializer.class)
    @Schema(description = "注册时间")
    private Instant registerTime;

    @JsonSerialize(using = InstantToTimestampSerializer.class)
    @Schema(description = "最后登录时间")
    private Instant lastLoginTime;

    @Column(nullable = false)
    @Schema(description = "用户状态")
    private UserStatus userStatus;

    @Column(nullable = false)
    @Schema(description = "是否管理员")
    private boolean isAdmin;

    public Users(String username, String password) {
        this.username = username;
        this.password = password;
        this.nickname = username; // 默认昵称为用户名
        this.registerTime = Instant.now();
        this.userStatus = UserStatus.NORMAL;
        this.wallet = BigDecimal.ZERO; // 默认钱包余额为0
        this.isAdmin = false; // 默认不是管理员
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Users users = (Users) o;
        return getUserId() != null && Objects.equals(getUserId(), users.getUserId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}