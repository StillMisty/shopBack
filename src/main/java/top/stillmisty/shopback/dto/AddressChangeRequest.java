package top.stillmisty.shopback.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@NotNull
public record AddressChangeRequest(
        @Size(min = 1, max = 100)
        String address,

        @Size(min = 1, max = 50)
        String name,

        @Size(min = 1, max = 20)
        String phone
) {
}