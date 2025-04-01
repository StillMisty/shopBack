package top.stillmisty.shopback.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.stillmisty.shopback.entity.Users;
import top.stillmisty.shopback.repository.UserRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

    public boolean isValidAvatar(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (contentType.startsWith("image/"));
    }

    public boolean changeAvatar(UUID userId, MultipartFile avatarFile) throws IOException {
        // 确保上传目录存在
        String uploadDir = "src/main/resources/static/avatars/";
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // 获取文件后缀
        String originalFilename = avatarFile.getOriginalFilename();
        String extension = null;
        if (originalFilename != null) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 生成新的文件名
        String filename = userId + extension;

        // 保存文件
        Path path = Paths.get(uploadDir + filename);
        Files.copy(avatarFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        // 更新用户头像URL
        String avatarUrl = "/avatars/" + filename;

        // 由于可能的文件后缀不同，删除旧头像文件
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        String oldAvatarUrl = user.getAvatar();
        if (oldAvatarUrl != null && !oldAvatarUrl.equals(avatarUrl)) {
            File oldAvatarFile = new File("src/main/resources/static" + oldAvatarUrl);
            if (oldAvatarFile.exists()) {
                oldAvatarFile.delete();
            }
        }

        if (userRepository.updateAvatar(userId, avatarUrl) > 0) {
            return true;
        } else {
            throw new RuntimeException("头像更新失败");
        }
    }
}
