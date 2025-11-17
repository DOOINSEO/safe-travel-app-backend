package com.webkit.travel_safety_backend.domain.service.Impl;

import com.webkit.travel_safety_backend.domain.model.dto.res.NewsItemResDTO;
import com.webkit.travel_safety_backend.domain.model.entity.ai.NewsItem;
import com.webkit.travel_safety_backend.domain.model.mapper.NewsMapper;
import com.webkit.travel_safety_backend.domain.repository.NewsRepository;
import com.webkit.travel_safety_backend.domain.service.Interface.NewsService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NewsServiceImpl implements NewsService {
    private final NewsRepository repo;
    private final NewsMapper mapper;

    @Override
    public NewsItemResDTO get(Long newsId) {
        NewsItem news = repo.findById(newsId).orElseThrow(() -> new EntityNotFoundException("news"));
        return mapper.toRes(news);
    }

    @Override
    public Page<NewsItemResDTO> getList(Integer page, Integer size, Boolean sort, String event, String regionCode, String q) {
        int p = (page == null ? 0 : page);
        int s = Math.min(size == null ? 10 : size, 20);
        String qTrim = (q == null || q.isBlank()) ? null : q.trim();

        Pageable pageable = sort
                ? PageRequest.of(p, s, Sort.by(Sort.Direction.DESC, "publishedAt").and(Sort.by(Sort.Direction.DESC, "id")))
                : PageRequest.of(p, s, Sort.by(Sort.Direction.ASC, "publishedAt").and(Sort.by(Sort.Direction.ASC, "id")));

        Specification<NewsItem> spec = Specification.allOf();
        if (regionCode != null)
            spec = spec.and((r, qy, cb) -> cb.equal(r.get("regionCode"), regionCode));
        if (event != null)
            spec = spec.and((r, qy, cb) -> cb.equal(r.get("eventClass"), event));
        if (qTrim != null) {
            String like = "%" + qTrim + "%";
            spec = spec.and((r, qy, cb) -> cb.like(r.get("titleKo"), like));
        }
        Page<NewsItem> paged  = repo.findAll(spec, pageable);

        List<NewsItemResDTO> content = paged.getContent().stream()
                .map(mapper::toRes).toList();

        return new PageImpl<>(content, pageable, paged.getTotalElements());
    }
}
