package six.daoyun.service.RefreshTokenServiceImpl;

import java.time.Instant;
import java.sql.Date;
import java.util.UUID;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import six.daoyun.entity.RefreshToken;
import six.daoyun.entity.User;
import six.daoyun.repository.RefreshTokenRepository;
import six.daoyun.service.RefreshTokenService;
import six.daoyun.service.UserService;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    @Autowired()
    private UserService userService;
    @Autowired()
    private RefreshTokenRepository refreshTokenRepository;

    private Date expireFromNow() {
        java.util.Date expire = java.util.Date.from(
                Instant.now().plusSeconds(2 * 24 * 60 * 60));
        Date ans = new Date(expire.getTime());
        return ans;
    }

    @Override
    public RefreshToken createRefreshToken(User user) {
        UUID corrId = UUID.randomUUID();
        RefreshToken token = new RefreshToken();
        token.setToken(corrId.toString());
        token.setUser(user);
        token.setExpiredDate(this.expireFromNow());

        this.refreshTokenRepository.save(token);

        return token;
    }
    @Override
    public RefreshToken createRefreshToken(Integer userId) {
        User user = this.userService.getUserByUserId(userId);
        return this.createRefreshToken(user);
    }
    @Override
    public Optional<RefreshToken> getRefreshTokenByToken(String token) {
        RefreshToken ans = this.refreshTokenRepository.getByToken(token);
        Date now = new Date(java.util.Date.from(Instant.now()).getTime());

        if(ans == null || ans.getExpiredDate().before(now)) {
            return Optional.empty();
        } else {
            return Optional.of(ans);
        }
    }

    @Override
    public void refreshRfreshToken(String token) {
        this.refreshTokenRepository.updateExpiredDateForToken(token, this.expireFromNow());
    }
    @Override
    public void deleteRefreshTokenByUser(User user) {
        this.refreshTokenRepository.deleteByUser_userName(user.getUserName());
    }

	@Override
	public void deleteRefreshTokenByToken(String token) {
        this.refreshTokenRepository.deleteByToken(token);
	}
}

