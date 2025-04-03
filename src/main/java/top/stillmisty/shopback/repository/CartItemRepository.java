package top.stillmisty.shopback.repository;

import org.springframework.data.repository.CrudRepository;
import top.stillmisty.shopback.entity.CartItem;
import top.stillmisty.shopback.entity.Users;

import java.util.List;
import java.util.UUID;

public interface CartItemRepository extends CrudRepository<CartItem, UUID> {
    List<CartItem> getCartItemsByUser(Users user);

    List<CartItem> findCartItemsByUser(Users user);
}
