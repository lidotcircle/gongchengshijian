package six.daoyun.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import six.daoyun.entity.User;


public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
}

