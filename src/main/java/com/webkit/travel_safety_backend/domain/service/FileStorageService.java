package com.webkit.travel_safety_backend.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
public class FileStorageService {
    private static final String RELATIVE_DIR = "/uploads/postImages";

    public String saveImage(MultipartFile file, Long postId, Integer order) {
        try {
            if (file == null || file.isEmpty())
                return RELATIVE_DIR + "/empty.jpg";

            String original = file.getOriginalFilename();
            String ext = StringUtils.getFilenameExtension(original);
            String safeExt = (ext != null && !ext.isBlank()) ? "." + ext.toLowerCase() : ".jpg";
            String fileName = postId + "_" + (order == null ? 0 : order) + safeExt;
            String base = System.getProperty("user.dir");
            Path dir = Paths.get(base + RELATIVE_DIR);
            Files.createDirectories(dir);
            Path target = dir.resolve(fileName).normalize();
            file.transferTo(target.toFile());
            return RELATIVE_DIR + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패", e);
        }
    }
}