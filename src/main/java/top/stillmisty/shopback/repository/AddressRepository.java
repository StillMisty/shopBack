package top.stillmisty.shopback.repository;

import org.springframework.data.repository.CrudRepository;
import top.stillmisty.shopback.entity.Users;

import java.util.UUID;

public interface AddressRepository extends CrudRepository<Users, UUID> {

}
