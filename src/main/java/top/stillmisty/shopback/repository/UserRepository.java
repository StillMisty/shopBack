package top.stillmisty.shopback.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import top.stillmisty.shopback.entity.Users;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<Users, UUID> {
    Optional<Users> findByUserName(String userName);

    @Modifying
    @Transactional
    @Query("UPDATE Users u SET u.avatar = :avatarUrl WHERE u.userId = :userId")
    int updateAvatar(UUID userId, String avatarUrl);
}
