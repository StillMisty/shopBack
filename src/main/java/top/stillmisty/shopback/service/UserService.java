package top.stillmisty.shopback.service;

import top.stillmisty.shopback.entity.User;
import top.stillmisty.shopback.repository.UserRepository;

public class UserService {
    final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean login(String userName, String password) {
        return userRepository.findByUserNameAndPassword(userName, password) != null;
    }

    public boolean register(String userName, String password) {
        if (!userRepository.findByUserName(userName).isEmpty()) {
            return false;
        }
        userRepository.save(new User(userName, password));
        return true;
    }

    public boolean changePassword(String userName, String oldPassword, String newPassword) {
        User user = userRepository.findByUserNameAndPassword(userName, oldPassword);
        if (user == null) {
            return false;
        }
        user.setPassword(newPassword);
        userRepository.save(user);
        return true;
    }

    public User findUserByUserName(String userName) {
        return userRepository.findByUserName(userName).get(0);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).get();
    }
}
