package com.webkit.travel_safety_backend.domain.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;

/**
 * FileStorageService
 * - postId_order.ext 규칙으로 이미지 관리
 * - 전체 파일 일괄 조회/삭제/저장 지원
 * - update 로직에서 사용하는 List 기반 버퍼링 지원
 */
@Service
public class FileStorageService {
    private static final String RELATIVE_DIR = "/uploads/postImages";
    private static final Pattern FILE_PATTERN = Pattern.compile("^(\\d+)_([0-9]+)\\.(.+)$"); // postId_order.ext

    private Path baseDir() {
        return Paths.get(System.getProperty("user.dir") + RELATIVE_DIR);
    }

    /** 메모리에 올려두는 이미지 버퍼 */
    public static class ImgData {
        public final byte[] bytes;
        public final String ext; // ".jpg" 형태
        public ImgData(byte[] bytes, String ext) {
            this.bytes = bytes;
            this.ext = ext != null && ext.startsWith(".") ? ext : ("." + (ext == null ? "jpg" : ext));
        }
    }

    /** 단건 저장: postId_order.ext */
    public String saveImage(MultipartFile file, Long postId, Integer order) {
        try {
            if (file == null || file.isEmpty()) return RELATIVE_DIR + "/empty.jpg";
            Files.createDirectories(baseDir());
            String original = file.getOriginalFilename();
            String ext = StringUtils.getFilenameExtension(original);
            String safeExt = (ext != null && !ext.isBlank()) ? "." + ext.toLowerCase() : ".jpg";
            String fileName = postId + "_" + (order == null ? 0 : order) + safeExt;
            Path target = baseDir().resolve(fileName).normalize();
            file.transferTo(target.toFile());
            return RELATIVE_DIR + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패", e);
        }
    }

    /** 단건 삭제 */
    public boolean deleteImage(String filePath) {
        if (filePath == null || filePath.isBlank()) return false;
        try {
            String name = getFileName(filePath);
            Path target = baseDir().resolve(name).normalize();
            return Files.deleteIfExists(target);
        } catch (Exception e) {
            return false;
        }
    }

    /** postId의 모든 파일 목록을 ImgData 리스트로 반환 (order 인덱스로 접근 가능) */
    public List<ImgData> getFileListByPostId(Long postId) {
        try {
            Files.createDirectories(baseDir());
            List<ImgData> list = new ArrayList<>();
            try (DirectoryStream<Path> ds = Files.newDirectoryStream(baseDir(), postId + "_*")) {
                for (Path p : ds) {
                    String name = p.getFileName().toString(); // ex) 10_2.jpg
                    Integer order = extractOrderFromPath(name);
                    if (order == null) continue;
                    ensureCapacity(list, order + 1);
                    byte[] bytes = Files.readAllBytes(p);
                    String ext = getExt(name);
                    list.set(order, new ImgData(bytes, ext));
                }
            }
            return list;
        } catch (IOException e) {
            throw new RuntimeException("파일 목록 조회 실패", e);
        }
    }

    /** postId의 모든 파일 삭제 */
    public void deleteByPostId(Long postId) {
        try {
            if (!Files.exists(baseDir())) return;
            try (DirectoryStream<Path> ds = Files.newDirectoryStream(baseDir(), postId + "_*")) {
                for (Path p : ds) Files.deleteIfExists(p);
            }
        } catch (IOException e) {
            throw new RuntimeException("파일 일괄 삭제 실패", e);
        }
    }

    /** ImgData 리스트를 일괄 저장 (order 인덱스 기준). null 슬롯은 건너뜀. */
    public Map<Integer, String> saveList(Long postId, List<ImgData> newImageList) {
        Map<Integer, String> result = new HashMap<>();
        if (newImageList == null) return result;
        try {
            Files.createDirectories(baseDir());
            for (int order = 0; order < newImageList.size(); order++) {
                ImgData img = newImageList.get(order);
                if (img == null) continue;
                String fileName = postId + "_" + order + img.ext.toLowerCase();
                Path target = baseDir().resolve(fileName).normalize();
                Files.write(target, img.bytes);
                result.put(order, RELATIVE_DIR + "/" + fileName);
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException("파일 일괄 저장 실패", e);
        }
    }

    /** 경로에서 order 추출 */
    public Integer extractOrderFromPath(String imgPath) {
        if (imgPath == null) return null;
        String name = getFileName(imgPath);
        int us = name.indexOf('_');
        int dot = name.lastIndexOf('.');
        if (us < 0) return null;
        String ord = (dot > us) ? name.substring(us + 1, dot) : name.substring(us + 1);
        try {
            return Integer.parseInt(ord);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public String getFileName(String imgPath) {
        return Paths.get(imgPath).getFileName().toString();
    }

    private String getExt(String name) {
        int dot = name.lastIndexOf('.');
        return (dot >= 0 ? name.substring(dot) : ".jpg");
    }

    private <T> void ensureCapacity(List<T> list, int size) {
        while (list.size() < size) list.add(null);
    }
}