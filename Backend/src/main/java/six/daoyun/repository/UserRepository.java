package six.daoyun.repository;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;
import six.daoyun.entity.User;


public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
    User getUserByUserName(String userName);
    User getUserByPhone(String phone);

    @Transactional
    void deleteByUserName(String userName);
}

