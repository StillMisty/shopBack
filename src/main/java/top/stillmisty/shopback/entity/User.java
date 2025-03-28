package top.stillmisty.shopback.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String userName;

    @Column(nullable = false)
    private String password;

    private String email;
    private String phone;
    private String nickname;
    private String avatar;

    @Column(nullable = false)
    private LocalDateTime registerTime;

    private LocalDateTime lastLoginTime;

    private Integer userStatus;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.registerTime = LocalDateTime.now();
    }
}