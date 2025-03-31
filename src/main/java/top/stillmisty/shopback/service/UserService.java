package top.stillmisty.shopback.service;

import org.springframework.stereotype.Service;
import top.stillmisty.shopback.entity.Users;
import top.stillmisty.shopback.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Users info(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }
}
