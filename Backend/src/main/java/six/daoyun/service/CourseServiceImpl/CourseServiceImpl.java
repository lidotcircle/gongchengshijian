package six.daoyun.service.CourseServiceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import six.daoyun.entity.Course;
import six.daoyun.entity.CourseStudent;
import six.daoyun.entity.User;
import six.daoyun.exception.Forbidden;
import six.daoyun.exception.NotFound;
import six.daoyun.repository.CourseRepository;
import six.daoyun.repository.CourseStudentRepository;
import six.daoyun.service.CourseService;
import six.daoyun.utils.MiscUtil;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseStudentRepository csRepository;

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
        if(csRepository.findByCourseAndStudent(course, student).isPresent()){
            throw new Forbidden("已在班课中");
        }

        final CourseStudent sc = new CourseStudent();
        sc.setGrade(0);
        sc.setCourse(course);
        sc.setStudent(student);

        this.csRepository.save(sc);
	}

	@Override
	public void exitCourse(Course course, User student) {
        final CourseStudent sc = csRepository.findByCourseAndStudent(course, student)
            .orElseThrow(() -> new NotFound("退出班课错误, 原本就不在班课中"));

        this.csRepository.delete(sc);
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
	public Page<Course> getTeacherOrStudentCoursePage(User user, int pageno, int size, String sortKey, boolean desc,
			String filter) {
        Sort sort = Sort.by(sortKey);
        if(desc) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }
        Pageable page = PageRequest.of(pageno, size, sort);
        if(filter == null || filter.isBlank()) {
            return this.courseRepository.findDistinctByStudents_StudentOrTeacher(user, user, page);
        } else {
            return this.courseRepository.findDistinctByStudents_StudentOrTeacher(filter, user, user, page);
        }
	}

	@Override
	public Page<Course> getCourseStudentPage(User student, int pageno, int size, String sortKey, boolean desc,
			String filter) {
        Sort sort = Sort.by(sortKey);
        if(desc) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }
        Pageable page = PageRequest.of(pageno, size, sort);
        if(filter == null || filter.isBlank()) {
            return this.courseRepository.findByStudents_Student(student, page);
        } else {
            return this.courseRepository.findByStudents_Student(filter, student, page);
        }
	}

	@Override
	public Optional<Course> getCourse(String courseExId) {
        return Optional.ofNullable(this.courseRepository.findByCourseExId(courseExId));
	}

	@Override
	public boolean courseHasStudent(Course course, User student) {
        return this.csRepository.findByCourseAndStudent(course, student).isPresent();
	}

	@Override
	public boolean isMemberOfCourse(Course course, User user) {
        return course.getTeacher().equals(user) || this.courseHasStudent(course, user);
	}

	@Override
	public boolean isMemberOfCourse(String courseExId, User user) {
        final Course course = this.getCourse(courseExId)
            .orElseThrow(() -> new NotFound("不是班课的成员"));
        return this.isMemberOfCourse(course, user);
	}
}
