package six.daoyun.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import six.daoyun.entity.CourseTask;


public interface CourseTaskRepository extends PagingAndSortingRepository<CourseTask, Long> {
}

