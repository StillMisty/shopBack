package top.stillmisty.shopback.service;

import org.springframework.stereotype.Service;
import top.stillmisty.shopback.dto.AddressChangeRequest;
import top.stillmisty.shopback.entity.Address;
import top.stillmisty.shopback.entity.Users;
import top.stillmisty.shopback.repository.AddressRepository;
import top.stillmisty.shopback.repository.UserRepository;

import java.util.List;
import java.util.UUID;

@Service
public class AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public AddressService(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    public List<Address> findByUser_UserId(UUID userId) {
        return addressRepository.findByUser_UserId(userId);
    }

    public Address findByAddressId(Long addressId) {
        return addressRepository.findByAddressId(addressId)
                .orElseThrow(() -> new RuntimeException("地址不存在"));
    }

    public void deleteByAddressId(Long addressId) {
        addressRepository.deleteByAddressId(addressId);
    }

    /**
     * 新增地址
     *
     * @param name    收货人姓名
     * @param address 收货地址
     * @param phone   收货人电话
     * @param userId  用户ID
     * @return 新增的地址
     */
    public Address save(String name, String address, String phone, UUID userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        Address newAddress = new Address(name, address, phone, user);
        return addressRepository.save(newAddress);
    }

    public Address save(Address currentAddress) {
        return addressRepository.save(currentAddress);
    }

    /**
     * 删除地址
     *
     * @param addressId 地址信息
     * @param userId    用户ID
     * @return 新增的地址
     */
    public void deleteAddress(Long addressId, UUID userId) {
        Address address = findByAddressId(addressId);
        if (address == null) {
            throw new RuntimeException("地址不存在");
        }
        if (!address.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("没有权限删除该地址");
        }
        deleteByAddressId(addressId);
    }

    /**
     * 更新地址
     *
     * @param addressId            地址ID
     * @param addressChangeRequest 地址信息
     * @param userId               用户ID
     * @return 更新的地址
     */
    public Address updateAddress(Long addressId, AddressChangeRequest addressChangeRequest, UUID userId) {
        Address currentAddress = findByAddressId(addressId);
        if (currentAddress == null) {
            throw new RuntimeException("地址不存在");
        }
        if (!currentAddress.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("没有权限修改该地址");
        }
        currentAddress.setName(addressChangeRequest.name());
        currentAddress.setAddress(addressChangeRequest.address());
        currentAddress.setPhone(addressChangeRequest.phone());
        return save(currentAddress);
    }
}
