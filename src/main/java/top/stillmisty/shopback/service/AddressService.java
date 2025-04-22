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
        Address address = findByAddressIdAndUserId(addressId, userId);
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
        Address currentAddress = findByAddressIdAndUserId(addressId, userId);
        currentAddress.setName(addressChangeRequest.name());
        currentAddress.setAddress(addressChangeRequest.address());
        currentAddress.setPhone(addressChangeRequest.phone());
        return save(currentAddress);
    }

    /**
     * 设置或取消默认地址
     *
     * @param addressId  地址ID
     * @param userId     用户ID
     * @param setDefault 是否设为默认地址
     * @return 更新后的地址
     */
    public Address updateDefaultStatus(Long addressId, UUID userId, boolean isDefault) {
        // 验证地址是否属于当前用户
        Address address = findByAddressIdAndUserId(addressId, userId);

        if (isDefault) {
            // 重置该用户的所有默认地址
            List<Address> userAddresses = findByUser_UserId(userId);
            userAddresses.stream()
                    .filter(Address::isDefault)
                    .forEach(addr -> {
                        addr.setDefault(false);
                        addressRepository.save(addr);
                    });

            // 设置新的默认地址
            address.setDefault(true);
        } else {
            // 取消默认状态
            address.setDefault(false);
        }

        return addressRepository.save(address);
    }

    /**
     * 根据地址ID和用户ID查找地址
     *
     * @param addressId 地址ID
     * @param userId    用户ID
     * @return 地址对象
     */
    private Address findByAddressIdAndUserId(Long addressId, UUID userId) {
        return addressRepository.findByAddressIdAndUser_UserId(addressId, userId)
                .orElseThrow(() -> new RuntimeException("地址不存在或不属于当前用户"));
    }
}
