package six.daoyun.controller.course;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import six.daoyun.controller.DYUtil;
import six.daoyun.controller.exception.HttpForbidden;
import six.daoyun.controller.exception.HttpNotFound;
import six.daoyun.controller.exception.HttpUnauthorized;
import six.daoyun.entity.Course;
import six.daoyun.entity.User;
import six.daoyun.service.CourseService;
import six.daoyun.service.UserService;


@Tag(name = "班课学生", description = "邀请、踢出、加入、退出")
@RestController()
@RequestMapping("/apis/course/student")
public class CourseStudentCrud {
    @Autowired
    private CourseService courseService;
    @Autowired
    private UserService userService;

    static class InviteDTO //{
    {
        @NotNull
        @Pattern(regexp = "\\d{10,}")
        private String courseExId;
        public String getCourseExId() {
            return this.courseExId;
        }
        public void setCourseExId(String courseExId) {
            this.courseExId = courseExId;
        }

        @NotNull
        private String studentName;
        public String getStudentName() {
            return this.studentName;
        }
        public void setStudentName(String studentName) {
            this.studentName = studentName;
        }
    } //}
    @PostMapping()
    private void inviteStudent(HttpServletRequest httpreq, 
            @RequestBody @Valid InviteDTO invitation) //{
    {
        final Course course = this.courseService.getCourse(invitation.getCourseExId())
            .orElseThrow(() -> new HttpNotFound("找不到课程"));

        final User user = DYUtil.getHttpRequestUser(httpreq);

        if(!course.getTeacher().getUserName().equals(user.getUserName())) {
            throw new HttpUnauthorized("不是课程的教师");
        }

        this.inviteStudentSuper(invitation);
    } //}

    @PostMapping("/super")
    private void inviteStudentSuper(@RequestBody @Valid InviteDTO invitation) //{
    {
        final Course course = this.courseService.getCourse(invitation.getCourseExId())
            .orElseThrow(() -> new HttpNotFound("找不到课程"));
        final User student = this.userService.getUser(invitation.getStudentName())
            .orElseThrow(() -> new HttpNotFound("找不到学生用户"));

        if(student.equals(course.getTeacher())) {
            throw new HttpForbidden("教师不可以加入自己的课程");
        }

        this.courseService.joinIntoCourse(course, student);
    } //}

    @DeleteMapping()
    private void deleteStudent(HttpServletRequest httpreq,
            @RequestParam("courseExId") String courseExId, 
            @RequestParam("studentName") String studentName) //{
    {
        final Course course = this.courseService.getCourse(courseExId)
            .orElseThrow(() -> new HttpNotFound("找不到课程"));
        final User user = DYUtil.getHttpRequestUser(httpreq);

        if(!course.getTeacher().getUserName().equals(user.getUserName())) {
            throw new HttpUnauthorized("不是课程的教师");
        }

        this.deleteStudentSuper(courseExId, studentName);
    } //}

    @DeleteMapping("/super")
    private void deleteStudentSuper(@RequestParam("courseExId") String courseExId, 
                                    @RequestParam("studentName") String studentName) //{
    {
        final Course course = this.courseService.getCourse(courseExId)
            .orElseThrow(() -> new HttpNotFound("找不到课程"));
        final User student = this.userService.getUser(studentName)
            .orElseThrow(() -> new HttpNotFound("找不到学生用户"));

        this.courseService.exitCourse(course, student);
    } //}

    static class JoinDTO //{
    {
        @NotNull
        @Pattern(regexp = "\\d{10,}")
        private String courseExId;
        public String getCourseExId() {
            return this.courseExId;
        }
        public void setCourseExId(String courseExId) {
            this.courseExId = courseExId;
        }
    } //}
    @PostMapping("/me")
    private void joinInto(HttpServletRequest httpreq, @RequestBody @Valid JoinDTO join) //{
    {
        final User user = DYUtil.getHttpRequestUser(httpreq);

        final Course course = this.courseService.getCourse(join.getCourseExId())
            .orElseThrow(() -> new HttpNotFound("找不到课程"));

        if(course.getTeacher().equals(user)) {
            throw new HttpForbidden("教师不可以加入自己的班课");
        }

        this.courseService.joinIntoCourse(course, user);
    } //}

    @DeleteMapping("/me")
    private void exit(HttpServletRequest httpreq,
            @RequestParam("courseExId") String courseExId) //{
    {
        final User user = DYUtil.getHttpRequestUser(httpreq);

        final Course course = this.courseService.getCourse(courseExId)
            .orElseThrow(() -> new HttpNotFound("找不到课程"));

        this.courseService.exitCourse(course, user);
    } //}
}

