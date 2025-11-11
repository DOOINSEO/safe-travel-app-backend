package com.webkit.travel_safety_backend.domain.security.filter;

import com.webkit.travel_safety_backend.domain.model.entity.RefreshTokenEntity;
import com.webkit.travel_safety_backend.domain.repository.RefreshTokenRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.channels.NonWritableChannelException;
import java.util.Date;

//Token을 DB에 저장
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenEntity getRefreshTokenEntity(Long userId) {
        return refreshTokenRepository.getRefreshTokenByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Refresh token not found"));
    }

    public RefreshTokenEntity saveRefreshTokenEntity(RefreshTokenEntity entity) {
        return refreshTokenRepository.save(entity);
    }

    @Transactional
    public RefreshTokenEntity updateAccessToken(Long userId, String accessToken) {
        return updateRefreshToken(userId, accessToken, null);
    }

    @Transactional
    public RefreshTokenEntity updateRefreshToken(Long userId, Date refreshTokenExpiration) {
        return this.updateRefreshToken(userId, null, refreshTokenExpiration);
    }

    @Transactional
    public RefreshTokenEntity updateRefreshToken(Long userId, String accessToken, Date refreshTokenExpiration)  {
        if (userId == null) throw new NullPointerException("userId is null");

        RefreshTokenEntity refreshTokenEntity = this.getRefreshTokenEntity(userId);

        if (accessToken != null) refreshTokenEntity.setAccessToken(accessToken);
        if (refreshTokenExpiration != null) refreshTokenEntity.setExpiration(refreshTokenExpiration);
        return refreshTokenRepository.save(refreshTokenEntity);
    }

    @Transactional
    public void deleteRefreshToken(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }

    @Transactional
    public void deleteRefreshToken(RefreshTokenEntity entity) {
        refreshTokenRepository.delete(entity);
    }
}
