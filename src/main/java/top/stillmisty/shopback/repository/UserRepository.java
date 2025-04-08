package top.stillmisty.shopback.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import top.stillmisty.shopback.entity.Users;
import top.stillmisty.shopback.enums.UserStatus;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<Users, UUID> {
    Optional<Users> findByUsername(String userName);

    Page<Users> findAll(Pageable pageable);

    Page<Users> findByUserStatus(UserStatus status, Pageable pageable);

    Page<Users> findByUsernameContaining(String username, Pageable pageable);

    boolean existsByUsername(String username);
}
