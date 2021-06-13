package six.daoyun.controller.course;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.springframework.beans.factory.annotation.Autowired;
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
import six.daoyun.entity.CommitTask;
import six.daoyun.entity.Course;
import six.daoyun.entity.CourseTask;
import six.daoyun.entity.User;
import six.daoyun.service.CourseService;
import six.daoyun.service.CourseTaskService;
import six.daoyun.utils.ObjUtil;

@RestController
@RequestMapping("/apis/course/task")
public class TaskCrud {
    @Autowired
    private CourseTaskService courseTaskService;
    @Autowired
    private CourseService courseService;

    static class TaskPostDTO //{
    {
        @NotNull
        private String courseExId;
        public String getCourseExId() {
            return this.courseExId;
        }
        public void setCourseExId(String courseExId) {
            this.courseExId = courseExId;
        }

        @NotNull
        private String taskTitle;
        public String getTaskTitle() {
            return this.taskTitle;
        }
        public void setTaskTitle(String taskTitle) {
            this.taskTitle = taskTitle;
        }

        private boolean committable;
        public boolean getCommittable() {
            return this.committable;
        }
        public void setCommittable(boolean committable) {
            this.committable = committable;
        }

        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", shape = JsonFormat.Shape.STRING)
        private Date deadline;
        public Date getDeadline() {
            return this.deadline;
        }
        public void setDeadline(Date deadline) {
            this.deadline = deadline;
        }

        private String content;
        public String getContent() {
            return this.content;
        }
        public void setContent(String content) {
            this.content = content;
        }
    } //}
    static class TaskPutDTO //{
    {
        @NotNull
        private long taskId;
        public long getTaskId() {
            return this.taskId;
        }
        public void setTaskId(long taskId) {
            this.taskId = taskId;
        }

        private String taskTitle;
        public String getTaskTitle() {
            return this.taskTitle;
        }
        public void setTaskTitle(String taskTitle) {
            this.taskTitle = taskTitle;
        }

        private Date deadline;
        public Date getDeadline() {
            return this.deadline;
        }
        public void setDeadline(Date deadline) {
            this.deadline = deadline;
        }

        private String content;
        public String getContent() {
            return this.content;
        }
        public void setContent(String content) {
            this.content = content;
        }
    } //}
    static class TaskGetDTO extends TaskPutDTO //{
    {
        private boolean committable;
        public boolean getCommittable() {
            return this.committable;
        }
        public void setCommittable(boolean committable) {
            this.committable = committable;
        }
    } //}

    private TaskIdResp createTask(TaskPostDTO newtask, User teacher, boolean bypassTeacher) //{
    {
        final Course course = this.courseService.getCourse(newtask.getCourseExId())
            .orElseThrow(() -> new HttpNotFound("课程不存在"));

        if(!bypassTeacher && !course.getTeacher().equals(teacher)) {
            throw new HttpForbidden("不是改课程的教师");
        }

        final CourseTask ctask = new CourseTask();
        ObjUtil.assignFields(ctask, newtask);
        ctask.setCourse(course);
        final long taskId = this.courseTaskService.createCourseTask(ctask);
        final TaskIdResp ans = new TaskIdResp();
        ans.setTaskId(taskId);
        return ans;
    } //}
    static class TaskIdResp //{
    {
        private long taskId;
        public long getTaskId() {
            return this.taskId;
        }
        public void setTaskId(long taskId) {
            this.taskId = taskId;
        }
    } //}
    @PostMapping()
    private TaskIdResp createTask(HttpServletRequest httpreq, @RequestBody @Valid TaskPostDTO newtask) //{
    {
        final User user = DYUtil.getHttpRequestUser(httpreq);
        return this.createTask(newtask, user, false);
    } //}

    @PostMapping("/super")
    private TaskIdResp createTaskSuper(@RequestBody @Valid TaskPostDTO newtask,
            @RequestParam("userName") String userName) //{
    {
        return this.createTask(newtask, null, true);
    } //}


    @DeleteMapping()
    private void deleteTask(HttpServletRequest httpreq,
            @RequestParam() long taskId) //{
    {
        final User user = DYUtil.getHttpRequestUser(httpreq);
        final CourseTask task = this.courseTaskService.getCourseTask(taskId)
            .orElseThrow(() -> new HttpNotFound("课程任务不存在"));

        if(!task.getCourse().getTeacher().equals(user)) {
            throw new HttpForbidden("不是该课程教师");
        }

        this.courseTaskService.deleteCourseTask(taskId);
    } //}

    @DeleteMapping("/super")
    private void deleteTask(@RequestParam() long taskId) //{
    {
        this.courseTaskService.deleteCourseTask(taskId);
    } //}


    @GetMapping()
    private TaskGetDTO getTask(HttpServletRequest httpreq,
            @RequestParam() long taskId) //{
    {
        final User user = DYUtil.getHttpRequestUser(httpreq);
        final CourseTask task = this.courseTaskService.getCourseTask(taskId)
            .orElseThrow(() -> new HttpNotFound("课程不存在"));

        if(!this.courseService.isMemberOfCourse(task.getCourse(), user)) {
            throw new HttpForbidden("不是该课程的成员");
        }

        return this.getTask(taskId);
    } //}
    @GetMapping("/super")
    private TaskGetDTO getTask(@RequestParam() long taskId) //{
    {
        final CourseTask task = this.courseTaskService.getCourseTask(taskId)
            .orElseThrow(() -> new HttpNotFound("课程不存在"));

        final TaskGetDTO ans = new TaskGetDTO();
        ObjUtil.assignFields(ans, task);

        return ans;
    } //}


    @PutMapping()
    private void updateTask(HttpServletRequest httpreq, @RequestBody @Valid TaskPutDTO utask) //{
    {
        final User user = DYUtil.getHttpRequestUser(httpreq);
        final CourseTask task = this.courseTaskService.getCourseTask(utask.getTaskId())
            .orElseThrow(() -> new HttpNotFound("找不到该课程任务"));

        if(!task.getCourse().getTeacher().equals(user)) {
            throw new HttpForbidden("不是该课程的教师");
        }

        this.updateTask(utask);
    } //}
    @PutMapping("/super")
    private void updateTask(@RequestBody @Valid TaskPutDTO utask) //{
    {
        final CourseTask task = this.courseTaskService.getCourseTask(utask.getTaskId())
            .orElseThrow(() -> new HttpNotFound("找不到该课程"));

        ObjUtil.assignFields(task, utask);
        this.courseTaskService.updateCourseTask(task);
    } //}

    static class StudentInfo //{
    {
        private String name;
        public String getName() {
            return this.name;
        }
        public void setName(String name) {
            this.name = name;
        }

        private String userName;
        public String getUserName() {
            return this.userName;
        }
        public void setUserName(String userName) {
            this.userName = userName;
        }

        private String studentNo;
        public String getStudentNo() {
            return this.studentNo;
        }
        public void setStudentNo(String studentNo) {
            this.studentNo = studentNo;
        }

        public StudentInfo(User student) {
            this.name = student.getName();
            this.userName = student.getUserName();
            this.studentNo = student.getStudentTeacherId();
        }
    } //}
    static class TaskAnwser //{
    {
        private long taskAnwserId;
        public long getTaskAnwserId() {
            return this.taskAnwserId;
        }
        public void setTaskAnwserId(long taskAnwserId) {
            this.taskAnwserId = taskAnwserId;
        }

        private String commitContent;
        public String getCommitContent() {
            return this.commitContent;
        }
        public void setCommitContent(String commitContent) {
            this.commitContent = commitContent;
        }

        private String teacherDoThis;
        public String getTeacherDoThis() {
            return this.teacherDoThis;
        }
        public void setTeacherDoThis(String teacherDoThis) {
            this.teacherDoThis = teacherDoThis;
        }

        private long grade;
        public long getGrade() {
            return this.grade;
        }
        public void setGrade(long grade) {
            this.grade = grade;
        }

        private StudentInfo studentInfo;
        public StudentInfo getStudentInfo() {
            return this.studentInfo;
        }
        public void setStudentInfo(StudentInfo studentInfo) {
            this.studentInfo = studentInfo;
        }
    } //}
    @GetMapping("/anwser-list")
    private Collection<TaskAnwser> getAnwserList(HttpServletRequest httpreq, @RequestParam() long taskId) //{
    {
        final User user = DYUtil.getHttpRequestUser(httpreq);

        final CourseTask task = this.courseTaskService.getCourseTask(taskId)
            .orElseThrow(() -> new HttpNotFound("找不到该课程任务"));

        if(!this.courseService.isMemberOfCourse(task.getCourse(), user)) {
            throw new HttpForbidden("不是该课程的成员");
        }

        return this.getAnwserListSuper(taskId);
    } //}

    @GetMapping("/anwser-list/super")
    private Collection<TaskAnwser> getAnwserListSuper(@RequestParam() long taskId) //{
    {
        final CourseTask task = this.courseTaskService.getCourseTask(taskId)
            .orElseThrow(() -> new HttpNotFound("找不到该课程任务"));

        final Collection<TaskAnwser> ans = new ArrayList<>();
        task.getCommitTasks().forEach((anwser) -> {
            StudentInfo s = new StudentInfo(anwser.getStudent());
            TaskAnwser a = new TaskAnwser();
            ObjUtil.assignFields(a, anwser);
            a.setTaskAnwserId(anwser.getId());
            a.setStudentInfo(s);
            ans.add(a);
        });

        return ans;
    } //}

    @GetMapping("/anwser")
    private TaskAnwser getAnwser(HttpServletRequest httpreq, @RequestParam() long taskId) //{
    {
        final User user = DYUtil.getHttpRequestUser(httpreq);

        final CourseTask task = this.courseTaskService.getCourseTask(taskId)
            .orElseThrow(() -> new HttpNotFound("找不到该课程任务"));

        if(!this.courseService.courseHasStudent(task.getCourse(), user)) {
            throw new HttpForbidden("不是该课程的学生");
        }

        final CommitTask anwser = this.courseTaskService.getCommitTaskByTaskAndStudent(taskId, user)
            .orElseThrow(() -> new HttpNotFound("该任务暂时还没有提交记录"));

        final TaskAnwser ans = new TaskAnwser();
        ObjUtil.assignFields(ans, anwser);
        ans.setTaskAnwserId(anwser.getId());
        return ans;
    } //}
}

