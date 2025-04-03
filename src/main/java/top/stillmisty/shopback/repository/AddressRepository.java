package top.stillmisty.shopback.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import top.stillmisty.shopback.entity.Address;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressRepository extends CrudRepository<Address, Long> {
    // 根据用户ID查找地址
    List<Address> findByUser_UserId(UUID userId);

    // 根据地址ID查找地址
    Optional<Address> findByAddressId(Long addressId);

    // 根据地址ID删除地址
    void deleteByAddressId(Long addressId);
}
