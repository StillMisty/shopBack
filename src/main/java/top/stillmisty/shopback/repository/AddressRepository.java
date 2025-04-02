package top.stillmisty.shopback.repository;

import org.springframework.data.repository.CrudRepository;
import top.stillmisty.shopback.entity.Address;

import java.util.List;
import java.util.UUID;

public interface AddressRepository extends CrudRepository<Address, Long> {
    // 根据用户ID查找地址
    List<Address> findByUser_UserId(UUID userId);

    // 根据地址ID查找地址
    Address findByAddressId(Long addressId);

    // 根据地址ID删除地址
    void deleteByAddressId(Long addressId);
}
