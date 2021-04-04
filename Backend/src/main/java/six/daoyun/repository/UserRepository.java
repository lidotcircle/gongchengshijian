package six.daoyun.repository;

import org.springframework.data.repository.CrudRepository;

import six.daoyun.entity.User;

public interface UserRepository extends CrudRepository<User, Integer> {
}

