package top.stillmisty.shopback.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "addresses")
@Data
public class Address {
    @Id
    @GeneratedValue
    @Schema(description = "地址 ID")
    private Long addressId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
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
    }

    public Address() {

    }
}
