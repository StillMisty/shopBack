package top.stillmisty.shopback.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "addresses")
@Data
public class Address {
    @Id
    @GeneratedValue
    private Long addressId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private Users user;

    private String address;
    private String name;
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
