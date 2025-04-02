package top.stillmisty.shopback.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import top.stillmisty.shopback.dto.AddressAddRequest;
import top.stillmisty.shopback.dto.AddressResponse;
import top.stillmisty.shopback.dto.AddressUpdateRequest;
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
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getAddress() {
        UUID userId = AuthUtils.getCurrentUserId();
        List<AddressResponse> addressResponseList = addressService.findByUser_UserId(userId).stream()
                .map(address -> new AddressResponse(
                        address.getAddressId(),
                        address.getName(),
                        address.getAddress(),
                        address.getPhone()))
                .toList();
        return ResponseEntity.ok(ApiResponse.success(addressResponseList));
    }

    @PostMapping
    @Operation(summary = "添加地址")
    @Transactional
    public ResponseEntity<ApiResponse<AddressResponse>> addAddress(@RequestBody AddressAddRequest addressRequest) {
        UUID userId = AuthUtils.getCurrentUserId();
        Address address = addressService.save(addressRequest.name(), addressRequest.address(), addressRequest.phone(), userId);
        AddressResponse addressResponse = new AddressResponse(
                address.getAddressId(),
                address.getName(),
                address.getAddress(),
                address.getPhone()
        );
        return ResponseEntity.ok(ApiResponse.success(addressResponse));
    }

    @DeleteMapping("/{addressId}")
    @Operation(summary = "删除地址")
    @Transactional
    public ResponseEntity<ApiResponse<Void>> deleteAddress(@Parameter(description = "要删除的地址ID")
                                                           @PathVariable Long addressId) {
        addressService.deleteAddress(addressId, AuthUtils.getCurrentUserId());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping
    @Operation(summary = "修改地址")
    @Transactional
    public ResponseEntity<ApiResponse<AddressResponse>> updateAddress(@RequestBody AddressUpdateRequest addressUpdateRequest) {
        Address updatedAddress = addressService.updateAddress(addressUpdateRequest, AuthUtils.getCurrentUserId());
        AddressResponse addressResponse = new AddressResponse(
                updatedAddress.getAddressId(),
                updatedAddress.getName(),
                updatedAddress.getAddress(),
                updatedAddress.getPhone()
        );
        return ResponseEntity.ok(ApiResponse.success(addressResponse));
    }
}
