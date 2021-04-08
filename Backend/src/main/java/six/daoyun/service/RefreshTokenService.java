package six.daoyun.service;

import java.util.Optional;

import six.daoyun.entity.RefreshToken;
import six.daoyun.entity.User;

public interface RefreshTokenService {
    RefreshToken createRefreshToken(User user);
    RefreshToken createRefreshToken(Integer userId);
    Optional<RefreshToken> getRefreshTokenByToken(String token);

    void refreshRfreshToken(String token);
    void deleteRefreshTokenByUser(User user);
    void deleteRefreshTokenByToken(String token);
}

