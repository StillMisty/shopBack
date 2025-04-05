package top.stillmisty.shopback.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import top.stillmisty.shopback.entity.Users;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<Users, UUID> {
    Optional<Users> findByUsername(String userName);
}
