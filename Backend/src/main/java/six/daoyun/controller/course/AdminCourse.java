package six.daoyun.controller.course;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import six.daoyun.controller.exception.HttpNotFound;
import six.daoyun.entity.Course;
import six.daoyun.entity.User;
import six.daoyun.service.CourseService;
import six.daoyun.service.UserService;
import six.daoyun.utils.ObjUitl;


@RestController
public class AdminCourse {
    @Autowired
    private CourseService courseService;
    @Autowired
    private UserService userService;

    static class CourseDTOX //{
    {
        private String courseExId;
        public String getCourseExId() {
            return this.courseExId;
        }
        public void setCourseExId(String courseExId) {
            this.courseExId = courseExId;
        }

        @NotNull
        private String courseName;
        public String getCourseName() {
            return this.courseName;
        }
        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }

        @NotNull
        private String teacherName;
        public String getTeacherName() {
            return this.teacherName;
        }
        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }

        private String briefDescription;
        public String getBriefDescription() {
            return this.briefDescription;
        }
        public void setBriefDescription(String briefDescription) {
            this.briefDescription = briefDescription;
        }
    } //}

    static class Teacher //{
    {
        private String userName;
        public String getUserName() {
            return this.userName;
        }
        public void setUserName(String userName) {
            this.userName = userName;
        }

        private String name;
        public String getName() {
            return this.name;
        }
        public void setName(String name) {
            this.name = name;
        }
    } //}
    static class Student //{
    {
        private String userName;
        public String getUserName() {
            return this.userName;
        }
        public void setUserName(String userName) {
            this.userName = userName;
        }
        private String name;
        public String getName() {
            return this.name;
        }
        public void setName(String name) {
            this.name = name;
        }

        private String studentTeacherId;
        public String getStudentTeacherId() {
            return this.studentTeacherId;
        }
        public void setStudentTeacherId(String studentTeacherId) {
            this.studentTeacherId = studentTeacherId;
        }
    } //}

    static class CourseDTO //{
    {
        private String courseExId;
        public String getCourseExId() {
            return this.courseExId;
        }
        public void setCourseExId(String courseExId) {
            this.courseExId = courseExId;
        }

        private String courseName;
        public String getCourseName() {
            return this.courseName;
        }
        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }

        private Teacher teacher;
        public Teacher getTeacher() {
            return this.teacher;
        }
        public void setTeacher(Teacher teacher) {
            this.teacher = teacher;
        }

        private Collection<Student> students;
        public Collection<Student> getStudents() {
            return this.students;
        }
        public void setStudents(Collection<Student> students) {
            this.students = students;
        }
    } //}
    static class CoursePageDTO //{
    {
        private Collection<CourseDTO> pairs;
        public Collection<CourseDTO> getPairs() {
            return this.pairs;
        }
        public void setPairs(Collection<CourseDTO> pairs) {
            this.pairs = pairs;
        }

        private long total;
        public long getTotal() {
            return this.total;
        }
        public void setTotal(long total) {
            this.total = total;
        }
    } //}

    @GetMapping("/apis/course")
    public CourseDTO getCourse(@RequestParam("courseExId") String courseExId) //{
    {
        final CourseDTO ans = new CourseDTO();
        final Course course = this.courseService.getCourse(courseExId).orElseThrow(() -> new HttpNotFound("找不到课程"));
        ans.setCourseExId(courseExId);
        ans.setCourseName(course.getCourseName());

        final Teacher teacher = new Teacher();
        teacher.setUserName(course.getTeacher().getUserName());
        teacher.setName(course.getTeacher().getName());
        ans.setTeacher(teacher);

        final Collection<Student> students = new ArrayList<>();
        course.getStudents().forEach((student) -> {
            Student s = new Student();
            ObjUitl.assignFields(s, student);
            students.add(s);
        });
        ans.setStudents(students);

        return ans;
    } //}

    static class CourseExIdDTO //{
    {
        private String courseExId;
        public String getCourseExId() {
            return this.courseExId;
        }
        public void setCourseExId(String courseExId) {
            this.courseExId = courseExId;
        }
    } //}
    @PostMapping("/apis/course")
    public CourseExIdDTO createCourse(@RequestBody @Valid CourseDTOX coursex) //{
    {
        final Course course = new Course();
        ObjUitl.assignFields(course, coursex);
        final User teacher = this.userService.getUser(coursex.getTeacherName())
            .orElseThrow(() -> new HttpNotFound("教师不存在"));
        course.setTeacher(teacher);

        this.courseService.createCourse(course);

        final CourseExIdDTO ans = new CourseExIdDTO();
        ans.setCourseExId(course.getCourseExId());
        return ans;
    } //}

    @PutMapping("/apis/course")
    public void updateCourse(@RequestBody CourseDTOX coursex) //{
    {
        final Course course = this.courseService.getCourse(coursex.getCourseExId())
            .orElseThrow(() -> new HttpNotFound("课程不存在"));
        ObjUitl.assignFields(course, coursex);
        if(coursex.getTeacherName() != null) {
            final User teacher = this.userService.getUser(coursex.getTeacherName())
                .orElseThrow(() -> new HttpNotFound("教师不存在"));

            course.setTeacher(teacher);
        }
        this.courseService.updateCourse(course);
    } //}

    @DeleteMapping("/apis/course")
    public void deleteCourse(@RequestParam("courseExId") String courseExId) //{
    {
        this.courseService.deleteCourse(courseExId);
    } //}
}

