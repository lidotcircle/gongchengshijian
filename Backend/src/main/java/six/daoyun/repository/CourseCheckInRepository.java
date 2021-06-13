package six.daoyun.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import six.daoyun.entity.CourseCheckIn;


public interface CourseCheckInRepository extends PagingAndSortingRepository<CourseCheckIn, Long> {
}

