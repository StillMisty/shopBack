package top.stillmisty.shopback.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
public class Users {

    @Id
    @GeneratedValue
    private UUID userId;

    @Column(nullable = false, unique = true, length = 20)
    private String username;

    @Column(nullable = false)
    @JsonIgnore  // 忽略密码的序列化
    private String password;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal wallet;

    @Column(nullable = false, length = 20)
    private String nickname;

    private String avatar;

    @Column(nullable = false)
    private LocalDateTime registerTime;

    private LocalDateTime lastLoginTime;

    private Integer userStatus;

    @Column(nullable = false)
    private boolean isAdmin;

    public Users(String username, String password) {
        this.username = username;
        this.password = password;
        this.nickname = username; // 默认昵称为用户名
        this.registerTime = LocalDateTime.now();
        this.userStatus = 1; // 默认状态为1
        this.wallet = BigDecimal.ZERO; // 默认钱包余额为0
        this.isAdmin = false; // 默认不是管理员
    }

    public Users() {
        this.registerTime = LocalDateTime.now();
    }
}