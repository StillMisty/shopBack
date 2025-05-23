package top.stillmisty.shopback.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import top.stillmisty.shopback.entity.Order;
import top.stillmisty.shopback.enums.OrderStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends CrudRepository<Order, UUID> {
    List<Order> findByUser_UserId(UUID userId, Sort sort);

    Page<Order> findAll(Pageable pageable);

    Page<Order> findAllByOrderStatus(OrderStatus orderStatus, Pageable pageable);

    Optional<Order> findByOrderIdAndUser_UserId(UUID orderId, UUID userId);
}
