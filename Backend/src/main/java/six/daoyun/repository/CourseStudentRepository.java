package six.daoyun.repository;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;

import six.daoyun.entity.Course;
import six.daoyun.entity.CourseStudent;
import six.daoyun.entity.User;


public interface CourseStudentRepository extends PagingAndSortingRepository<CourseStudent, CourseStudent.CourseStudentId> {
    Optional<CourseStudent> findByCourse_CourseExIdAndStudent(String CourseExId, User Student);
    Optional<CourseStudent> findByCourseAndStudent(Course course, User Student);

    @Transactional
    void deleteByCourseAndStudent(Course course, User student);
}

