package six.daoyun.repository;

import java.sql.Date;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import six.daoyun.entity.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Integer> {
    RefreshToken getByToken(String token);

    @Modifying
    @Query("update RefreshToken t set t.expiredDate = ?2 where t.token = ?1")
    void updateExpiredDateForToken(String token, Date newExpiredDate);

    void deleteByToken(String token);
    void deleteByUser_userName(String userName);
}

