package top.stillmisty.shopback.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class PictureUtils {

    public static boolean isValidPicture(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.startsWith("image/");
    }

    public static String getFileExtension(MultipartFile file) throws RuntimeException {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename != null) {
            return originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        throw new RuntimeException("文件名不能为空");
    }

    // 保存图片，并返回文件名
    public static String savePicture(String prefix, MultipartFile file, Path directory) throws IOException {
        if (file.isEmpty()) {
            throw new RuntimeException("文件不能为空");
        }

        if (!PictureUtils.isValidPicture(file)) {
            throw new RuntimeException("文件类型不合法");
        }
        String filename = prefix + PictureUtils.getFileExtension(file);
        Path path = Paths.get(directory + "/" + filename);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        return filename;
    }
}
