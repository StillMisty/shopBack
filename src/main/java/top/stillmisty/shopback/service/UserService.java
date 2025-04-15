package top.stillmisty.shopback.service;

import cn.hutool.core.util.IdUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.stillmisty.shopback.entity.Users;
import top.stillmisty.shopback.enums.UserStatus;
import top.stillmisty.shopback.repository.UserRepository;
import top.stillmisty.shopback.utils.PictureUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    private final Path avatarPath;

    @Value("${app.base-url}")
    private String baseUrl;

    public UserService(UserRepository userRepository, @Value("${file.upload-dir}") String uploadDir) {
        this.userRepository = userRepository;
        this.avatarPath = Paths.get(uploadDir + "/avatars");
        try {
            if (!Files.exists(avatarPath)) {
                Files.createDirectories(avatarPath);
            }
        } catch (IOException e) {
            throw new RuntimeException("无法创建头像目录", e);
        }
    }

    public Users info(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
    }

    public Users changePassword(UUID userId, String password) {
        Users existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        existingUser.setPassword(password);
        return userRepository.save(existingUser);
    }

    public Users changeNickname(UUID userId, String nickname) {
        Users existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        existingUser.setNickname(nickname);
        return userRepository.save(existingUser);
    }

    public Users changeAvatar(UUID userId, MultipartFile avatarFile) throws IOException {

        String filename = PictureUtils.savePicture(IdUtil.simpleUUID(), avatarFile, avatarPath);

        // 更新用户头像URL
        String avatarUrl = baseUrl + "/public/avatars/" + filename;

        Users existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        // 删除旧头像文件
        if (existingUser.getAvatar() != null) {
            String oldAvatarPath = existingUser.getAvatar().replace(baseUrl + "/public/avatars/", "");
            Files.deleteIfExists(avatarPath.resolve(oldAvatarPath));
        }
        existingUser.setAvatar(avatarUrl);
        return userRepository.save(existingUser);
    }

    public Page<Users> getAllUsers(int page, int size) {
        return userRepository.findAll(PageRequest.of(page, size));
    }

    public Page<Users> getUsersByStatus(UserStatus status, int page, int size) {
        return userRepository.findByUserStatus(status, PageRequest.of(page, size));
    }

    public Page<Users> searchUsers(String username, int page, int size) {
        return userRepository.findByUsernameContaining(username, PageRequest.of(page, size));
    }

    public Users updateUserStatus(UUID userId, UserStatus status) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setUserStatus(status);
        return userRepository.save(user);
    }

    public Users setAdminRole(UUID userId, boolean isAdmin) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setAdmin(isAdmin);
        return userRepository.save(user);
    }

    public Users resetUserPassword(UUID userId, String newPassword) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        user.setPassword(newPassword);
        return userRepository.save(user);
    }
}
