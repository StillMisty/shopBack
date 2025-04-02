package top.stillmisty.shopback.dto;

public record AddressResponse(
        Long addressId,
        String name,
        String address,
        String phone
) {
}
