package top.stillmisty.shopback.dto;

import jakarta.validation.constraints.Size;

public record AddressAddRequest(
        @Size(min = 1, max = 100)
        String address,

        @Size(min = 1, max = 50)
        String name,

        @Size(min = 1, max = 20)
        String phone
) {
}