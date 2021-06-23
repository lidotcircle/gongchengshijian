package six.daoyun.repository;


import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import six.daoyun.entity.Course;
import six.daoyun.entity.User;

public interface CourseRepository extends PagingAndSortingRepository<Course, Integer> {
    @Query("SELECT cr FROM Course cr WHERE cr.teacher.userName LIKE %?1% OR cr.teacher.name LIKE %?1% OR cr.courseName LIKE %?1%")
    Page<Course> findAll(String filter, Pageable request);
    Page<Course> findAll(Pageable request);

    @Query("SELECT cr FROM Course cr WHERE cr.courseName LIKE %?1%")
    Page<Course> findByTeacher(String filter, User teacher, Pageable request);
    Page<Course> findByTeacher(User teacher, Pageable request);

    @Query("SELECT cr FROM Course cr WHERE cr.teacher.userName LIKE %?1% OR cr.teacher.name LIKE %?1% OR cr.courseName LIKE %?1%")
    Page<Course> findByStudents_Student(String filter, User students, Pageable request);
    Page<Course> findByStudents_Student(User students, Pageable request);

    Course findByCourseExId(String exId);

    @Transactional
    void deleteByCourseExId(String courseExId);
}

