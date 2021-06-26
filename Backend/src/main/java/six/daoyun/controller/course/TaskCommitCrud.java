package six.daoyun.controller.course;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import six.daoyun.controller.DYUtil;
import six.daoyun.controller.exception.HttpForbidden;
import six.daoyun.controller.exception.HttpNotFound;
import six.daoyun.entity.CommitTask;
import six.daoyun.entity.User;
import six.daoyun.service.CourseTaskService;
import six.daoyun.service.UserService;
import six.daoyun.utils.ObjUtil;


@Tag(name = "学生任务", description = "提交班课任务...")
@RestController
@RequestMapping("/apis/course/task/anwser")
public class TaskCommitCrud {
    @Autowired
    private CourseTaskService courseTaskService;
    @Autowired
    private UserService userService;

    static class TaskAnwserPostDTO //{
    {
        @NotNull
        private long taskId;
        public long getTaskId() {
            return this.taskId;
        }
        public void setTaskId(long taskId) {
            this.taskId = taskId;
        }

        @NotNull
        private String commitContent;
        public String getCommitContent() {
            return this.commitContent;
        }
        public void setCommitContent(String commitContent) {
            this.commitContent = commitContent;
        }
    } //}
    static class TaskAnwserPutDTO extends TaskAnwserPostDTO //{
    {
        @NotNull
        private long taskAnwserId;
        public long getTaskAnwserId() {
            return this.taskAnwserId;
        }
        public void setTaskAnwserId(long taskAnwserId) {
            this.taskAnwserId = taskAnwserId;
        }

        @NotNull
        private String commitContent;
        public String getCommitContent() {
            return this.commitContent;
        }
        public void setCommitContent(String commitContent) {
            this.commitContent = commitContent;
        }
    } //}
    static class TaskAnwserGetDTO extends TaskAnwserPutDTO //{
    {
        private long grade;
        public long getGrade() {
            return this.grade;
        }
        public void setGrade(long grade) {
            this.grade = grade;
        }

        private String teacherDoThis;
        public String getTeacherDoThis() {
            return this.teacherDoThis;
        }
        public void setTeacherDoThis(String teacherDoThis) {
            this.teacherDoThis = teacherDoThis;
        }
    } //}
    
    private TaskAnwserIdResp commitTask(TaskAnwserPostDTO anwser, User user) //{
    {
        final CommitTask canwser = new CommitTask();
        ObjUtil.assignFields(canwser, anwser);

        final long taskAnwserId = this.courseTaskService.commitTask(anwser.getTaskId(), user, canwser);
        final TaskAnwserIdResp ans = new TaskAnwserIdResp();
        ans.setTaskAnwserId(taskAnwserId);
        return ans;
    } //}
    static class TaskAnwserIdResp //{
    {
        private long taskAnwserId;
        public long getTaskAnwserId() {
            return this.taskAnwserId;
        }
        public void setTaskAnwserId(long taskAnwserId) {
            this.taskAnwserId = taskAnwserId;
        }
    } //}
    @PostMapping()
    private TaskAnwserIdResp createTask(HttpServletRequest httpreq, @RequestBody @Valid TaskAnwserPostDTO newtask) //{
    {
        final User user = DYUtil.getHttpRequestUser(httpreq);
        return this.commitTask(newtask, user);
    } //}

    @PostMapping("/super")
    private TaskAnwserIdResp createTaskSuper(@RequestBody @Valid TaskAnwserPostDTO newtask,
            @RequestParam("userName") String userName) //{
    {
        final User user = this.userService.getUser(userName)
            .orElseThrow(() -> new HttpNotFound("用户不存在"));
        return this.commitTask(newtask, user);
    } //}


    @DeleteMapping()
    private void deleteTaskAnwser(HttpServletRequest httpreq,
            @RequestParam() long taskAnwserId) //{
    {
        final User user = DYUtil.getHttpRequestUser(httpreq);
        final CommitTask anwser = this.courseTaskService.getCommitTask(taskAnwserId)
            .orElseThrow(() -> new HttpNotFound("不存在该任务提交"));

        if(!anwser.getStudent().equals(user)) {
            throw new HttpForbidden("不是该任务的提交者");
        }

        this.courseTaskService.deleteCommitTask(taskAnwserId);
    } //}

    @DeleteMapping("/super")
    private void deleteTask(@RequestParam() long taskAnwserId) //{
    {
        this.courseTaskService.deleteCourseTask(taskAnwserId);
    } //}


    @GetMapping()
    private TaskAnwserGetDTO getTask(HttpServletRequest httpreq,
            @RequestParam() long taskAnwserId) //{
    {
        final User user = DYUtil.getHttpRequestUser(httpreq);
        final CommitTask anwser = this.courseTaskService.getCommitTask(taskAnwserId)
            .orElseThrow(() -> new HttpNotFound("任务提交不存在"));

        if(!anwser.getStudent().equals(user) && !anwser.getCourseTask().getCourse().getTeacher().equals(user)) {
            throw new HttpForbidden("无权限查看提交的任务");
        }

        return this.getTaskAnwser(taskAnwserId);
    } //}
    @GetMapping("/super")
    private TaskAnwserGetDTO getTaskAnwser(@RequestParam() long taskAnwserId) //{
    {
        final CommitTask anwser = this.courseTaskService.getCommitTask(taskAnwserId)
            .orElseThrow(() -> new HttpNotFound("任务提交不存在"));

        final TaskAnwserGetDTO ans = new TaskAnwserGetDTO();
        ObjUtil.assignFields(ans, anwser);

        return ans;
    } //}


    @PutMapping()
    private void updateTaskAnwser(HttpServletRequest httpreq, @RequestBody @Valid TaskAnwserPutDTO uanwser) //{
    {
        final User user = DYUtil.getHttpRequestUser(httpreq);
        final CommitTask anwser = this.courseTaskService.getCommitTask(uanwser.getTaskAnwserId())
            .orElseThrow(() -> new HttpNotFound("找不到该任务提交"));

        if(!anwser.getStudent().equals(user)) {
            throw new HttpForbidden("无权修改提交");
        }

        this.updateTaskAnwser(uanwser);
    } //}
    @PutMapping("/super")
    private void updateTaskAnwser(@RequestBody @Valid TaskAnwserPutDTO uanwser) //{
    {
        final CommitTask anwser = this.courseTaskService.getCommitTask(uanwser.getTaskAnwserId())
            .orElseThrow(() -> new HttpNotFound("找不到该任务提交"));

        ObjUtil.assignFields(anwser, uanwser);
        this.courseTaskService.updateCommitTask(anwser);
    } //}
}

