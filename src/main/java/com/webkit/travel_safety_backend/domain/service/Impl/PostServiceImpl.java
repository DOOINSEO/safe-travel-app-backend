package com.webkit.travel_safety_backend.domain.service.Impl;

import com.webkit.travel_safety_backend.domain.model.dto.req.PostImageReqDTO;
import com.webkit.travel_safety_backend.domain.model.dto.req.PostReqDTO;
import com.webkit.travel_safety_backend.domain.model.dto.res.PostResDTO;
import com.webkit.travel_safety_backend.domain.model.entity.*;
import com.webkit.travel_safety_backend.domain.model.mapper.PostImageMapper;
import com.webkit.travel_safety_backend.domain.model.mapper.PostMapper;
import com.webkit.travel_safety_backend.domain.repository.*;
import com.webkit.travel_safety_backend.domain.service.FileStorageService;
import com.webkit.travel_safety_backend.domain.service.Interface.PostService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final PostCategoryRepository postCategoryRepository;
    private final LocationRepository locationRepository;
    private final FileStorageService fileStorageService;
    private final PostMapper mapper;
    private final PostImageMapper postImageMapper;

    @Override
    public PostResDTO create(Long userId, PostReqDTO reqDTO) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("user"));
        PostCategory postCategory = postCategoryRepository.findById(reqDTO.getCategoryId()).orElseThrow(() -> new EntityNotFoundException("category"));
        Locations location = locationRepository.findByRegionCode(reqDTO.getRegionCode()).orElseThrow(() -> new EntityNotFoundException("location"));
        Posts post = Posts.builder()
                .content(reqDTO.getContent())
                .user(user)
                .category(postCategory)
                .location(location)
                .build();
        Posts saved = postRepository.save(post);

        if (reqDTO.getImages() != null && !reqDTO.getImages().isEmpty()) {
            int autoOrder = 0;
            for (PostImageReqDTO i : reqDTO.getImages()) {
                if (i.getFile() == null || i.getFile().isEmpty()) continue;
                int ord = (i.getOrder() == null) ? autoOrder++ : i.getOrder();
                String path = fileStorageService.saveImage(i.getFile(), saved.getId(), ord);
                PostImages img = PostImages.builder()
                        .post(saved)
                        .imgPath(path)
                        .order(ord)
                        .build();
                postImageRepository.save(img);
            }
        }
        return assembleRes(userId, post);
    }

    @Override
    public PostResDTO get(Long userId, Long postId) {
        Posts post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("post"));
        return assembleRes(userId, post);
    }

    @Override
    public Page<PostResDTO> getList(Long userId, Integer page, Integer size, String sort, String regionCode, Long categoryId, String q) {

        int p = (page == null ? 0 : page);
        int s = Math.min(size == null ? 10 : size, 20);
        String qTrim = (q == null || q.isBlank()) ? null : q.trim();

        if ("likeCount".equalsIgnoreCase(sort)) {
            Pageable pageable = PageRequest.of(p, s);
            Page<Posts> paged = postRepository.findAllOrderByLikeCountDesc(
                    categoryId, regionCode, qTrim, pageable
            );

            List<PostResDTO> content = paged.getContent().stream()
                    .map(post -> assembleRes(userId, post))
                    .toList();

            return new PageImpl<>(content, pageable, paged.getTotalElements());
        }

        Pageable pageable = PageRequest.of(p, s, Sort.by(Sort.Direction.DESC, "id"));

        Specification<Posts> spec = Specification.allOf();

        if (regionCode != null)
            spec = spec.and((r, qy, cb) -> cb.equal(r.get("location").get("regionCode"), regionCode));

        if (categoryId != null)
            spec = spec.and((r, qy, cb) -> cb.equal(r.get("category").get("id"), categoryId));

        if (qTrim != null) {
            String like = "%" + qTrim + "%";
            spec = spec.and((r, qy, cb) -> cb.like(r.get("content"), like));
        }

        Page<Posts> paged = postRepository.findAll(spec, pageable);

        List<PostResDTO> content = paged.getContent().stream()
                .map(post -> assembleRes(userId, post))
                .toList();

        return new PageImpl<>(content, pageable, paged.getTotalElements());
    }


    private static <T> void safeSet(List<T> list, int index, T value) {
        if (index < 0) throw new IllegalArgumentException("order must be >= 0");
        while (list.size() <= index) list.add(null);
        list.set(index, value);
    }

    @Override
    public PostResDTO update(Long userId, Long postId, PostReqDTO reqDTO) {
        Posts post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("post"));

        // 본문/메타 갱신
        if (reqDTO.getContent() != null) post.setContent(reqDTO.getContent());
        if (reqDTO.getCategoryId() != null)
            post.setCategory(postCategoryRepository.findById(reqDTO.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("category")));
        if (reqDTO.getRegionCode() != null)
            post.setLocation(locationRepository.findByRegionCode(reqDTO.getRegionCode())
                    .orElseThrow(() -> new EntityNotFoundException("location")));
        postRepository.save(post);

        if (reqDTO.getImages() == null) return assembleRes(userId, post);

        List<FileStorageService.ImgData> oldImageList = fileStorageService.getFileListByPostId(postId);
        List<PostImages> oldEntityList = postImageRepository.findByPost_IdOrderByOrderAsc(postId);

        Map<String, PostImages> oldEntityByPath = new HashMap<>();
        for (PostImages pi : oldEntityList) {
            String fname = Paths.get(pi.getImgPath()).getFileName().toString();
            oldEntityByPath.put(fname, pi);
            oldEntityByPath.put(pi.getImgPath(), pi);
        }

        List<FileStorageService.ImgData> newImageList = new ArrayList<>();
        List<PostImages> newEntityList = new ArrayList<>();
        Set<Integer> seen = new HashSet<>();

        for (PostImageReqDTO image : reqDTO.getImages()) {
            boolean hasPath = image.getImgPath() != null && !image.getImgPath().isBlank();
            boolean hasFile = image.getFile() != null && !image.getFile().isEmpty();
            if (!hasPath && !hasFile) continue;

            Integer newOrder = image.getOrder();
            if (newOrder == null || newOrder < 0)
                throw new IllegalArgumentException("order is required and must be >= 0");
            if (!seen.add(newOrder))
                throw new IllegalArgumentException("duplicated order in request: " + newOrder);

            if (hasPath) {
                String key = Paths.get(image.getImgPath()).getFileName().toString();
                PostImages existed = oldEntityByPath.getOrDefault(image.getImgPath(), oldEntityByPath.get(key));
                Integer oldOrder = (existed != null)
                        ? existed.getOrder()
                        : fileStorageService.extractOrderFromPath(image.getImgPath());

                FileStorageService.ImgData src = (oldOrder != null && oldOrder >= 0 && oldOrder < oldImageList.size())
                        ? oldImageList.get(oldOrder)
                        : null;
                if (src == null)
                    throw new IllegalStateException("source file buffer missing for oldOrder=" + oldOrder);

                safeSet(newImageList, newOrder, src);
                safeSet(newEntityList, newOrder,
                        PostImages.builder()
                                .post(post)
                                .order(newOrder)
                                .imgPath(postId + "_" + newOrder + src.ext)
                                .build());

            } else {
                try {
                    MultipartFile mf = image.getFile();
                    String original = mf.getOriginalFilename();
                    String ext = (original != null && original.lastIndexOf('.') >= 0)
                            ? original.substring(original.lastIndexOf('.')).toLowerCase()
                            : ".jpg";
                    FileStorageService.ImgData buf =
                            new FileStorageService.ImgData(mf.getBytes(), ext);
                    safeSet(newImageList, newOrder, buf);
                    safeSet(newEntityList, newOrder,
                            PostImages.builder()
                                    .post(post)
                                    .order(newOrder)
                                    .imgPath(postId + "_" + newOrder + ext)
                                    .build());
                } catch (Exception e) {
                    throw new RuntimeException("failed to buffer upload file (order=" + newOrder +
                            ", newImageList.size=" + newImageList.size() + ")", e);
                }
            }
        }

        fileStorageService.deleteByPostId(postId);
        postImageRepository.deleteByPost_Id(postId);
        postImageRepository.flush();

        Map<Integer, String> saved = fileStorageService.saveList(postId, newImageList);
        for (int ord = 0; ord < newEntityList.size(); ord++) {
            PostImages ent = newEntityList.get(ord);
            if (ent == null) continue;
            String finalPath = saved.get(ord);
            if (finalPath == null || finalPath.isBlank())
                throw new IllegalStateException("final saved path missing for order=" + ord);
            ent.setImgPath(finalPath);
            postImageRepository.save(ent);
        }

        return assembleRes(userId, post);
    }

    @Override
    public void delete(Long userId, Long postId) {
        Posts post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("post"));

        if (!Objects.equals(post.getUser().getId(), userId))
            throw new SecurityException("no Auth");

        // 1) 로컬 파일 먼저 삭제
        List<PostImages> images = postImageRepository.findByPost_IdOrderByOrderAsc(postId);
        for (PostImages img : images) {
            try {
                fileStorageService.deleteImage(img.getImgPath());
            } catch (Exception e) {
                log.warn("fail to delete local image: {}", img.getImgPath(), e);
            }
        }

        postImageRepository.deleteByPost_Id(postId);
        postImageRepository.flush();

        // 3) 게시글 삭제
        postRepository.delete(post);
    }

    private PostResDTO assembleRes(Long viewerId, Posts post) {
        PostResDTO dto = mapper.toRes(post);

        List<PostImages> images = postImageRepository.findByPost_IdOrderByOrderAsc(post.getId());
        dto.setImages(images.stream().map(postImageMapper::toRes).toList());

        int likeCnt = postLikeRepository.countByPost_Id(post.getId());
        dto.setLikeCount(likeCnt);

        boolean liked = viewerId != null && postLikeRepository.existsByUser_IdAndPost_Id(viewerId, post.getId());
        dto.setIsLike(liked);
        return dto;
    }
}
