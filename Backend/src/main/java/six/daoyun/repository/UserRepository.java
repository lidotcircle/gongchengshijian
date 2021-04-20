package six.daoyun.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import six.daoyun.entity.User;


public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
    User getUserByUserName(String userName);
    User getUserByPhone(String phone);

    @Query("SELECT u FROM User u WHERE u.userName LIKE %?1% OR u.name LIKE %?1% OR u.phone LIKE %?1%")
    Page<User> findAll(String filter, Pageable page);
    Page<User> findAll(Pageable page);

    @Transactional
    void deleteByUserName(String userName);
}

