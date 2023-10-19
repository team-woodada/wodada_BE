package com.me.wodada.auth.refreshToken;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void save(Long userId, String refreshToken, String email){
        RefreshToken token = RefreshToken.builder()
                .userId(userId)
                .refreshToken(refreshToken)
                .build();

        refreshTokenRepository.save(token);
    }

    @Transactional
    public void remove(Long userId){
        RefreshToken token = refreshTokenRepository.findByUserId(userId)
                .orElseThrow(RuntimeException::new);

        refreshTokenRepository.delete(token);
    }

    public RefreshToken findRefreshToken(Long memberId){
        return refreshTokenRepository.findByUserId(memberId)
                .orElseThrow(RuntimeException::new);
    }
}
