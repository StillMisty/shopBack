package top.stillmisty.shopback.repository;

import org.springframework.data.repository.CrudRepository;
import top.stillmisty.shopback.entity.Users;

import java.util.Optional;

public interface UserRepository extends CrudRepository<Users, Long> {
    Optional<Users> findByUserName(String userName);
}
