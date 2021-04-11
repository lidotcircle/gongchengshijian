package six.daoyun.repository;

import java.sql.Date;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import six.daoyun.entity.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Integer> {
    RefreshToken getByToken(String token);

    @Modifying
    @Query("update RefreshToken t set t.expiredDate = ?2 where t.token = ?1")
    void updateExpiredDateForToken(String token, Date newExpiredDate);

    @Transactional
    void deleteByToken(String token);
    @Transactional
    void deleteByUser_userName(String userName);
}

