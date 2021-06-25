package six.daoyun.controller.course;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import six.daoyun.controller.DYUtil;
import six.daoyun.controller.exception.HttpForbidden;
import six.daoyun.controller.exception.HttpNotFound;
import six.daoyun.entity.Course;
import six.daoyun.entity.User;
import six.daoyun.service.CourseService;
import six.daoyun.service.UserService;
import six.daoyun.utils.ObjUtil;


@RestController
@RequestMapping("/apis/course")
public class CourseCrud {
    @Autowired
    private CourseService courseService;
    @Autowired
    private UserService userService;


    static class PostCourseDTOX //{
    {
        private String courseExId;
        public String getCourseExId() {
            return this.courseExId;
        }
        public void setCourseExId(String courseExId) {
            this.courseExId = courseExId;
        }

        @NotNull
        @Pattern(regexp = "(\\w|\\p{sc=Han}){2,}")
        private String courseName;
        public String getCourseName() {
            return this.courseName;
        }
        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }

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
    static class PutCourseDTOX //{
    {
        @NotNull
        private String courseExId;
        public String getCourseExId() {
            return this.courseExId;
        }
        public void setCourseExId(String courseExId) {
            this.courseExId = courseExId;
        }

        @Pattern(regexp = "(\\w|\\p{sc=Han}){2,}")
        private String courseName;
        public String getCourseName() {
            return this.courseName;
        }
        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }

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

        private Long score;
        public Long getScore() {
            return this.score;
        }
        public void setScore(Long score) {
            this.score = score;
        }
    } //}

    static class CourseTaskDTO //{
    {
        private long id;
        public long getId() {
            return this.id;
        }
        public void setId(long id) {
            this.id = id;
        }

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", shape = JsonFormat.Shape.STRING)
        private Date releaseDate;
        public Date getReleaseDate() {
            return this.releaseDate;
        }
        public void setReleaseDate(Date releaseDate) {
            this.releaseDate = releaseDate;
        }

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", shape = JsonFormat.Shape.STRING)
        private Date deadline;
        public Date getDeadline() {
            return this.deadline;
        }
        public void setDeadline(Date deadline) {
            this.deadline = deadline;
        }

        private boolean committable;
        public boolean getCommittable() {
            return this.committable;
        }
        public void setCommittable(boolean committable) {
            this.committable = committable;
        }

        private String taskTitle;
        public String getTaskTitle() {
            return this.taskTitle;
        }
        public void setTaskTitle(String taskTitle) {
            this.taskTitle = taskTitle;
        }

        private String content;
        public String getContent() {
            return this.content;
        }
        public void setContent(String content) {
            this.content = content;
        }
    } //}
    static class CourseCheckinDTO //{
    {
        private long id;
        public long getId() {
            return this.id;
        }
        public void setId(long id) {
            this.id = id;
        }

        private String jsonData;
        public String getJsonData() {
            return this.jsonData;
        }
        public void setJsonData(String jsonData) {
            this.jsonData = jsonData;
        }

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", shape = JsonFormat.Shape.STRING)
        private Date releaseDate;
        public Date getReleaseDate() {
            return this.releaseDate;
        }
        public void setReleaseDate(Date releaseDate) {
            this.releaseDate = releaseDate;
        }

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", shape = JsonFormat.Shape.STRING)
        private Date deadline;
        public Date getDeadline() {
            return this.deadline;
        }
        public void setDeadline(Date deadline) {
            this.deadline = deadline;
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

        private String briefDescription;
        public String getBriefDescription() {
            return this.briefDescription;
        }
        public void setBriefDescription(String briefDescription) {
            this.briefDescription = briefDescription;
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

        private Collection<CourseTaskDTO> tasks;
        public Collection<CourseTaskDTO> getTasks() {
            return this.tasks;
        }
        public void setTasks(Collection<CourseTaskDTO> tasks) {
            this.tasks = tasks;
        }

        private Collection<CourseCheckinDTO> checkins;
        public Collection<CourseCheckinDTO> getCheckins() {
            return this.checkins;
        }
        public void setCheckins(Collection<CourseCheckinDTO> checkins) {
            this.checkins = checkins;
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

    private void course2courseDTO(final CourseDTO target, final Course course) //{
    {
        ObjUtil.assignFields(target, course);

        final Teacher teacher = new Teacher();
        teacher.setUserName(course.getTeacher().getUserName());
        teacher.setName(course.getTeacher().getName());
        target.setTeacher(teacher);

        final Collection<Student> students = new ArrayList<>();
        course.getStudents().forEach((student) -> {
            Student s = new Student();
            ObjUtil.assignFields(s, student.getStudent());
            s.setScore(student.getGrade());
            students.add(s);
        });
        target.setStudents(students);

        final Collection<CourseTaskDTO> tasks = new ArrayList<>();
        course.getTasks().forEach(task -> {
            CourseTaskDTO dtask = new CourseTaskDTO();
            ObjUtil.assignFields(dtask, task);
            dtask.setReleaseDate(task.getCreatedDate());
            tasks.add(dtask);
        });
        target.setTasks(tasks);

        final Collection<CourseCheckinDTO> checkins = new ArrayList<>();
        course.getCheckins().forEach(checkin -> {
            CourseCheckinDTO dcheckin = new CourseCheckinDTO();
            ObjUtil.assignFields(dcheckin, checkin);
            dcheckin.setReleaseDate(checkin.getModifiedDate());
            checkins.add(dcheckin);
        });
        target.setCheckins(checkins);
    } //}

    @GetMapping("/super")
    public CourseDTO getCourse(@RequestParam("courseExId") String courseExId) //{
    {
        final CourseDTO ans = new CourseDTO();
        final Course course = this.courseService.getCourse(courseExId).orElseThrow(() -> new HttpNotFound("找不到课程"));
        this.course2courseDTO(ans, course);
        return ans;
    } //}
    @GetMapping()
    public CourseDTO getCourse(HttpServletRequest httpreq,
            @RequestParam("courseExId") String courseExId) //{
    {
        User user = DYUtil.getHttpRequestUser(httpreq);
        if(!this.courseService.isMemberOfCourse(courseExId, user)) {
            throw new HttpForbidden("不是课程的成员");
        }

        return this.getCourse(courseExId);
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
    private CourseExIdDTO createCourse(PostCourseDTOX coursex, User teacher) //{
    {
        final Course course = new Course();
        ObjUtil.assignFields(course, coursex);
        course.setTeacher(teacher);

        this.courseService.createCourse(course);

        final CourseExIdDTO ans = new CourseExIdDTO();
        ans.setCourseExId(course.getCourseExId());
        return ans;
    } //}
    @PostMapping("/super")
    public CourseExIdDTO createCourse(@RequestBody @Valid PostCourseDTOX coursex) //{
    {
        if(coursex.getTeacherName() == null) {
            throw new HttpForbidden("require teacher name");
        }

        final User teacher = this.userService.getUser(coursex.getTeacherName())
            .orElseThrow(() -> new HttpNotFound("教师不存在"));

        return this.createCourse(coursex, teacher);
    } //}
    @PostMapping()
    public CourseExIdDTO createCourse(HttpServletRequest httpreq, 
            @RequestBody @Valid PostCourseDTOX coursex) //{
    {
        final User teacher = DYUtil.getHttpRequestUser(httpreq);

        return this.createCourse(coursex, teacher);
    } //}

    @PutMapping("/super")
    public void updateCourse(@RequestBody @Valid PutCourseDTOX coursex) //{
    {
        final Course course = this.courseService.getCourse(coursex.getCourseExId())
            .orElseThrow(() -> new HttpNotFound("课程不存在"));
        ObjUtil.assignFields(course, coursex);
        this.courseService.updateCourse(course);
    } //}
    @PutMapping()
    public void updateCourse(HttpServletRequest httpreq, 
            @RequestBody @Valid PutCourseDTOX coursex) //{
    {
        final User user = DYUtil.getHttpRequestUser(httpreq);
        final Course course = this.courseService.getCourse(coursex.getCourseExId())
            .orElseThrow(() -> new HttpNotFound("课程不存在"));
        if(!course.getTeacher().equals(user)) {
            throw new HttpForbidden("不是课程的老师");
        }
        ObjUtil.assignFields(course, coursex);
        this.courseService.updateCourse(course);
    } //}

    @DeleteMapping("/super")
    public void deleteCourse(@RequestParam("courseExId") String courseExId) //{
    {
        this.courseService.deleteCourse(courseExId);
    } //}
    @DeleteMapping()
    public void deleteCourse(HttpServletRequest httpreq, 
            @RequestParam("courseExId") String courseExId) //{
    {
        final User user = DYUtil.getHttpRequestUser(httpreq);
        final Course course = this.courseService.getCourse(courseExId)
            .orElseThrow(() -> new HttpNotFound("课程不存在"));
        if(!course.getTeacher().equals(user)) {
            throw new HttpForbidden("不是课程的老师");
        }
        this.deleteCourse(courseExId);
    } //}

    @GetMapping("/page")
    private CoursePageDTO getPageByStudentOrTeacher(HttpServletRequest httpreq,
            @RequestParam(value = "pageno", defaultValue = "1") int pageno, 
            @RequestParam(value = "size", defaultValue = "10") int size, 
            @RequestParam(value = "sortDir", required = false) String sortDir,
            @RequestParam(value = "sortKey", defaultValue = "courseName") String sortKey,
            @RequestParam(value = "searchWildcard", required = false) String wildcard) //{
    {
        CoursePageDTO ans = new CoursePageDTO();

        final User student = DYUtil.getHttpRequestUser(httpreq);
        Page<Course> page = this.courseService.getTeacherOrStudentCoursePage(student, pageno - 1, size, sortKey, 
                "desc".equalsIgnoreCase(sortDir), wildcard);

        Collection<CourseDTO> pairs = new ArrayList<>();
        page.forEach(v -> {
            CourseDTO res = new CourseDTO();
            this.course2courseDTO(res, v);
            pairs.add(res);
        });
        ans.setPairs(pairs);
        ans.setTotal(page.getTotalElements());

        return ans;
    } //}

    @GetMapping("/student/page")
    private CoursePageDTO getPageByStudent(HttpServletRequest httpreq,
            @RequestParam(value = "pageno", defaultValue = "1") int pageno, 
            @RequestParam(value = "size", defaultValue = "10") int size, 
            @RequestParam(value = "sortDir", required = false) String sortDir,
            @RequestParam(value = "sortKey", defaultValue = "courseName") String sortKey,
            @RequestParam(value = "searchWildcard", required = false) String wildcard) //{
    {
        CoursePageDTO ans = new CoursePageDTO();

        final User student = DYUtil.getHttpRequestUser(httpreq);
        Page<Course> page = this.courseService.getCourseStudentPage(student, pageno - 1, size, sortKey, 
                "desc".equalsIgnoreCase(sortDir), wildcard);

        Collection<CourseDTO> pairs = new ArrayList<>();
        page.forEach(v -> {
            CourseDTO res = new CourseDTO();
            this.course2courseDTO(res, v);
            pairs.add(res);
        });
        ans.setPairs(pairs);
        ans.setTotal(page.getTotalElements());

        return ans;
    } //}

    @GetMapping("/teacher/page")
    private CoursePageDTO getPageByTeacher(HttpServletRequest httpreq,
            @RequestParam(value = "pageno", defaultValue = "1") int pageno, 
            @RequestParam(value = "size", defaultValue = "10") int size, 
            @RequestParam(value = "sortDir", required = false) String sortDir,
            @RequestParam(value = "sortKey", defaultValue = "courseName") String sortKey,
            @RequestParam(value = "searchWildcard", required = false) String wildcard) //{
    {
        CoursePageDTO ans = new CoursePageDTO();

        final User teacher = DYUtil.getHttpRequestUser(httpreq);
        Page<Course> page = this.courseService.getTeacherCoursePage(teacher, pageno - 1, size, sortKey, 
                "desc".equalsIgnoreCase(sortDir), wildcard);

        Collection<CourseDTO> pairs = new ArrayList<>();
        page.forEach(v -> {
            CourseDTO res = new CourseDTO();
            this.course2courseDTO(res, v);
            pairs.add(res);
        });
        ans.setPairs(pairs);
        ans.setTotal(page.getTotalElements());

        return ans;
    } //}

    @GetMapping("/super/page")
    private CoursePageDTO getAllPageBySuper(HttpServletRequest httpreq,
            @RequestParam(value = "pageno", defaultValue = "1") int pageno, 
            @RequestParam(value = "size", defaultValue = "10") int size, 
            @RequestParam(value = "sortDir", required = false) String sortDir,
            @RequestParam(value = "sortKey", defaultValue = "courseName") String sortKey,
            @RequestParam(value = "searchWildcard", required = false) String wildcard) //{
    {
        CoursePageDTO ans = new CoursePageDTO();

        Page<Course> page = this.courseService.getCoursePage(pageno - 1, size, sortKey, 
                "desc".equalsIgnoreCase(sortDir), wildcard);
        Collection<CourseDTO> pairs = new ArrayList<>();
        page.forEach(v -> {
            CourseDTO res = new CourseDTO();
            this.course2courseDTO(res, v);
            pairs.add(res);
        });
        ans.setPairs(pairs);
        ans.setTotal(page.getTotalElements());

        return ans;
    } //}
}

