package top.stillmisty.shopback.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import top.stillmisty.shopback.dto.AddressChangeRequest;
import top.stillmisty.shopback.dto.ApiResponse;
import top.stillmisty.shopback.entity.Address;
import top.stillmisty.shopback.service.AddressService;
import top.stillmisty.shopback.utils.AuthUtils;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/address")
@Tag(name = "地址管理", description = "用户收货地址的增删改查接口")
public class AddressController {
    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping
    @Operation(summary = "获取地址列表")
    public ResponseEntity<ApiResponse<List<Address>>> getAddress() {

        UUID userId = AuthUtils.getCurrentUserId();
        List<Address> addressList = addressService.findByUser_UserId(userId);
        return ResponseEntity.ok(ApiResponse.success(addressList));
    }

    @GetMapping("/{addressId}")
    @Operation(summary = "获取地址详情")
    public ResponseEntity<ApiResponse<Address>> getAddressById(
            @Parameter(description = "要获取的地址ID")
            @PathVariable Long addressId
    ) {
        Address address = addressService.getAddressById(addressId, AuthUtils.getCurrentUserId());
        return ResponseEntity.ok(ApiResponse.success(address));
    }

    @PostMapping
    @Operation(summary = "添加地址")
    @Transactional
    public ResponseEntity<ApiResponse<Address>> addAddress(
            @Valid @RequestBody AddressChangeRequest addressChangeRequest
    ) {
        UUID userId = AuthUtils.getCurrentUserId();
        Address address = addressService.save(
                addressChangeRequest.name(),
                addressChangeRequest.address(),
                addressChangeRequest.phone(),
                userId
        );
        return ResponseEntity.ok(ApiResponse.success(address));
    }

    @DeleteMapping("/{addressId}")
    @Operation(summary = "删除地址")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Transactional
    public void deleteAddress(
            @Parameter(description = "要删除的地址ID")
            @PathVariable Long addressId
    ) {
        addressService.deleteAddress(addressId, AuthUtils.getCurrentUserId());
    }

    @PatchMapping("/{addressId}")
    @Operation(summary = "修改地址")
    @Transactional
    public ResponseEntity<ApiResponse<Address>> updateAddress(
            @Parameter(description = "要修改的地址ID")
            @PathVariable Long addressId,
            @RequestBody AddressChangeRequest addressChangeRequest
    ) {
        Address updatedAddress = addressService.updateAddress(
                addressId,
                addressChangeRequest,
                AuthUtils.getCurrentUserId()
        );
        return ResponseEntity.ok(ApiResponse.success(updatedAddress));
    }

    @PatchMapping("/{addressId}/set-default")
    @Operation(summary = "设置默认地址")
    @Transactional
    public ResponseEntity<ApiResponse<Address>> updateDefaultStatus(
            @Parameter(description = "要设置为默认的地址ID")
            @PathVariable Long addressId,
            @Parameter(description = "是否设为默认地址，true为设置，false为取消，默认true")
            @RequestParam(defaultValue = "true") boolean isDefault
    ) {
        Address defaultAddress = addressService.updateDefaultStatus(
                addressId,
                AuthUtils.getCurrentUserId(),
                isDefault
        );
        return ResponseEntity.ok(ApiResponse.success(defaultAddress));
    }
}
