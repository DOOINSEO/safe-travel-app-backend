package com.webkit.travel_safety_backend.domain.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webkit.travel_safety_backend.domain.model.dto.res.RiskEvalResDTO;
import com.webkit.travel_safety_backend.domain.model.entity.ai.RiskEvaluate;
import com.webkit.travel_safety_backend.domain.model.mapper.RiskEvalMapper;
import com.webkit.travel_safety_backend.domain.repository.RiskEvalRepository;
import com.webkit.travel_safety_backend.domain.service.Interface.RiskEvalService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RiskEvalServiceImpl implements RiskEvalService {

    private final RiskEvalRepository repo;
    private final RiskEvalMapper mapper;
    private final ObjectMapper objectMapper;

    @Override
    public RiskEvalResDTO get(String regionCode) {
        RiskEvaluate risk = repo.findByRegionCode(regionCode).orElseThrow(() -> new EntityNotFoundException("risk"));
        RiskEvalResDTO dto = mapper.toRes(risk);

        String detail = risk.getDetailsJson();
        if (detail == null || detail.isBlank()) {
            return dto;
        }

        BigDecimal mofaRisk = null;
        Integer mofaStage = null;

        BigDecimal newsR = null;
        Map<String, Long> newsCategoryCounts = new HashMap<>();

        BigDecimal gdacsR = null;
        Map<String, Long> gdacsTypeCounts = new HashMap<>();

        try {
            JsonNode root = objectMapper.readTree(detail);
            JsonNode sources = root.path("sources");

            JsonNode mofa = sources.path("MOFA");
            if (!mofa.isMissingNode()) {
                if (mofa.hasNonNull("risk")) {
                    mofaRisk = BigDecimal.valueOf(mofa.path("risk").asDouble());
                }
                if (mofa.hasNonNull("stage")) {
                    mofaStage = mofa.path("stage").asInt();
                }
            }

            JsonNode news = sources.path("NEWS");
            if (!news.isMissingNode()) {
                if (news.hasNonNull("R")) {
                    newsR = BigDecimal.valueOf(news.path("R").asDouble());
                }

                JsonNode items = news.path("items");
                if (items.isArray()) {
                    for (JsonNode item : items) {
                        String category = item.path("category").asText(null);
                        if (category != null) {
                            newsCategoryCounts.merge(category, 1L, Long::sum);
                        }
                    }
                }
            }

            JsonNode gdacs = sources.path("GDACS");
            if (!gdacs.isMissingNode()) {
                if (gdacs.hasNonNull("R")) {
                    gdacsR = BigDecimal.valueOf(gdacs.path("R").asDouble());
                }

                JsonNode events = gdacs.path("events");
                if (events.isArray()) {
                    for (JsonNode event : events) {
                        String type = event.path("type").asText(null);
                        if (type != null) {
                            // EQ(지진) TC(태풍) FL(홍수) VO(화산) WF(산불) DR(가뭄) TS(쓰나미)
                            if (type.equals("EQ")) type = "지진";
                            if (type.equals("TC")) type = "태풍";
                            if (type.equals("FL")) type = "홍수";
                            if (type.equals("VO")) type = "화산";
                            if (type.equals("WF")) type = "산불";
                            if (type.equals("DR")) type = "가뭄";
                            if (type.equals("TS")) type = "쓰나미";
                            gdacsTypeCounts.merge(type, 1L, Long::sum);
                        }
                    }
                }
            }

        } catch (JsonProcessingException e) {
            log.error("details_json 파싱 실패. regionCode={}, detail={}", regionCode, detail, e);
        }

        dto.setMofaRisk(mofaRisk);
        dto.setMofaStage(mofaStage);

        dto.setNewsR(newsR);
        dto.setNewsCategoryCounts(newsCategoryCounts.isEmpty() ? null : newsCategoryCounts);

        dto.setGdacsR(gdacsR);
        dto.setGdacsEpisodeCounts(gdacsTypeCounts.isEmpty() ? null : gdacsTypeCounts);

        String context = buildResultContext(dto);
        dto.setResultContext(context);
        return dto;
    }

    private String buildResultContext(RiskEvalResDTO dto) {
        StringBuilder sb = new StringBuilder();

        sb.append("외교부 위험도 ")
                .append(dto.getMofaRisk() != null ? dto.getMofaRisk() : "?")
                .append(" (단계 ")
                .append(dto.getMofaStage() != null ? dto.getMofaStage() : "?")
                .append("), ");

        sb.append("뉴스 위험도 ")
                .append(dto.getNewsR() != null ? dto.getNewsR() : "?")
                .append(", ");

        if (dto.getNewsCategoryCounts() != null && !dto.getNewsCategoryCounts().isEmpty()) {
            sb.append("뉴스 카테고리별 건수: ");
            dto.getNewsCategoryCounts().forEach((category, count) ->
                    sb.append(category).append(" ").append(count).append("건, ")
            );
        } else {
            sb.append("뉴스 데이터 없음, ");
        }

        sb.append("GDACS 위험도 ")
                .append(dto.getGdacsR() != null ? dto.getGdacsR() : "?")
                .append(", ");

        if (dto.getGdacsEpisodeCounts() != null && !dto.getGdacsEpisodeCounts().isEmpty()) {
            sb.append("GDACS 에피소드별 건수: ");
            dto.getGdacsEpisodeCounts().forEach((type, count) ->
                    sb.append(type).append(" ").append(count).append("건, ")
            );
        } else {
            sb.append("GDACS 이벤트 없음, ");
        }

        String result = sb.toString().trim();
        if (result.endsWith(",")) {
            result = result.substring(0, result.length() - 1);
        }

        return result;
    }

}