package top.stillmisty.shopback.dto;

public record AddressUpdateRequest(
        Long addressId,
        String name,
        String address,
        String phone
) {
}
