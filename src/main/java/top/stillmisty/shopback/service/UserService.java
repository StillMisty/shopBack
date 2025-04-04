package top.stillmisty.shopback.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.stillmisty.shopback.entity.Users;
import top.stillmisty.shopback.repository.UserRepository;
import top.stillmisty.shopback.utils.PictureUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

    public boolean changePassword(UUID userId, String password) {
        Users existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        existingUser.setPassword(password);
        return true;
    }

    public boolean changeNickname(UUID userId, String nickname) {
        Users existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        existingUser.setNickname(nickname);
        return true;
    }

    public boolean changeAvatar(UUID userId, MultipartFile avatarFile) throws IOException {
        if (avatarFile.isEmpty()) {
            throw new RuntimeException("文件不能为空");
        }

        if (!PictureUtils.isValidPicture(avatarFile)) {
            throw new RuntimeException("文件类型不合法");
        }

        // 生成新的文件名
        String filename = userId + PictureUtils.getFileExtension(avatarFile);

        // 保存文件
        Path path = Paths.get(avatarPath + "/" + filename);
        Files.copy(avatarFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        // 更新用户头像URL
        String avatarUrl = baseUrl + "/public/avatars/" + filename;

        if (userRepository.updateAvatar(userId, avatarUrl) > 0) {
            return true;
        } else {
            throw new RuntimeException("头像更新失败");
        }
    }
}
