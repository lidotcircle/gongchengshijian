package six.daoyun.repository;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import six.daoyun.entity.Course;
import six.daoyun.entity.User;

public interface CourseRepository extends PagingAndSortingRepository<Course, Integer> {
    @Query("SELECT cr FROM Course cr WHERE cr.courseName LIKE %?1%")
    Page<Course> findByTeacher(User user, String filter, Pageable request);
    Page<Course> findByTeacher(User user, Pageable request);

    @Query("SELECT cr FROM Course cr WHERE cr.teacher LIKE %?1% OR cr.courseName LIKE %?1%")
    Page<Course> findAll(String filter, Pageable request);
    Page<Course> findAll(Pageable request);

    Course findByCourseExId(String exId);

    @Transactional
    void deleteByCourseExId(String courseExId);
}

