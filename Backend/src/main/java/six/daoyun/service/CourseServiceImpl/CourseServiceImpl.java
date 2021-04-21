package six.daoyun.service.CourseServiceImpl;

import java.util.Optional;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import six.daoyun.entity.Course;
import six.daoyun.entity.User;
import six.daoyun.repository.CourseRepository;
import six.daoyun.service.CourseService;
import six.daoyun.utils.MiscUtil;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseRepository courseRepository;

	@Override
	public String createCourse(Course course) {
        String courseExId = MiscUtil.generateDigitString(10);
        course.setCourseExId(courseExId);
        this.courseRepository.save(course);
        return courseExId;
	}

	@Override
	public void updateCourse(Course course) {
        this.courseRepository.save(course);
	}

	@Override
	public void deleteCourse(String courseExId) {
        this.courseRepository.deleteByCourseExId(courseExId);
	}

	@Override
	public void joinIntoCourse(Course course, User student) {
        final Collection<User> students = course.getStudents();
        if(!students.contains(student)) {
            students.add(student);
            this.courseRepository.save(course);
        }
	}

	@Override
	public void exitCourse(Course course, User student) {
        final Collection<User> students = course.getStudents();
        if(students.contains(student)) {
            students.remove(student);
            this.courseRepository.save(course);
        }
	}

	@Override
	public Page<Course> getCoursePage(int pageno, int size, String sortKey, boolean desc, String filter) {
        Sort sort = Sort.by(sortKey);
        if(desc) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }
        Pageable page = PageRequest.of(pageno, size, sort);
        if(filter == null || filter.isBlank()) {
            return this.courseRepository.findAll(page);
        } else {
            return this.courseRepository.findAll(filter, page);
        }
	}

	@Override
	public Page<Course> getTeacherCoursePage(User teacher, int pageno, int size, String sortKey, boolean desc,
			String filter) {
        Sort sort = Sort.by(sortKey);
        if(desc) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }
        Pageable page = PageRequest.of(pageno, size, sort);
        if(filter == null || filter.isBlank()) {
            return this.courseRepository.findByTeacher(teacher, page);
        } else {
            return this.courseRepository.findByTeacher(filter, teacher, page);
        }
	}

	@Override
	public Page<Course> getStudentCoursePage(User student, int pageno, int size, String sortKey, boolean desc,
			String filter) {
        Sort sort = Sort.by(sortKey);
        if(desc) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }
        Pageable page = PageRequest.of(pageno, size, sort);
        if(filter == null || filter.isBlank()) {
            return this.courseRepository.findByStudents(student, page);
        } else {
            return this.courseRepository.findByStudents(filter, student, page);
        }
	}

	@Override
	public Optional<Course> getCourse(String courseExId) {
        return Optional.ofNullable(this.courseRepository.findByCourseExId(courseExId));
	}
}
