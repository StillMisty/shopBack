package top.stillmisty.shopback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.stillmisty.shopback.entity.Admin;

import java.util.Optional;
import java.util.UUID;

public interface AdminRepository extends JpaRepository<Admin, UUID> {
    Optional<Admin> findByUsername(String username);
}