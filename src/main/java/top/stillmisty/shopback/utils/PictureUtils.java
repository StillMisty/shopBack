package top.stillmisty.shopback.utils;

import org.springframework.web.multipart.MultipartFile;

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
}
