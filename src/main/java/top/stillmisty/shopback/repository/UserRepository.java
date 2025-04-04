package top.stillmisty.shopback.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import top.stillmisty.shopback.entity.Users;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<Users, UUID> {
    Optional<Users> findByUsername(String userName);

    @Modifying
    @Transactional
    @Query("UPDATE Users u SET u.avatar = :avatarUrl WHERE u.userId = :userId")
    int updateAvatar(UUID userId, String avatarUrl);
}
