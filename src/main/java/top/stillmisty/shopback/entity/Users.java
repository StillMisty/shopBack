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

    @Column(nullable = false, unique = true)
    private String userName;

    @Column(nullable = false)
    @JsonIgnore  // 忽略密码的序列化
    private String password;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal wallet;

    private String avatar;

    @Column(nullable = false)
    private LocalDateTime registerTime;

    private LocalDateTime lastLoginTime;

    private Integer userStatus;

    public Users(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.registerTime = LocalDateTime.now();
        this.userStatus = 1; // 默认状态为1
        this.wallet = BigDecimal.ZERO; // 默认钱包余额为0
    }

    public Users() {
        this.registerTime = LocalDateTime.now();
    }
}