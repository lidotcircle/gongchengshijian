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
import six.daoyun.entity.CheckinTask;
import six.daoyun.entity.User;
import six.daoyun.service.CourseCheckinService;
import six.daoyun.service.UserService;
import six.daoyun.utils.ObjUtil;


@Tag(name = "学生签到")
@RestController
@RequestMapping("/apis/course/check-in/anwser")
public class CheckinCommitCrud {
    @Autowired
    private CourseCheckinService courseCheckinService;
    @Autowired
    private UserService userService;

    static class CheckinAnwserPostDTO //{
    {
        @NotNull
        private long checkinId;
        public long getCheckinId() {
            return this.checkinId;
        }
        public void setCheckinId(long checkinId) {
            this.checkinId = checkinId;
        }

        @NotNull
        private String checkinJsonData;
        public String getCheckinJsonData() {
            return this.checkinJsonData;
        }
        public void setCheckinJsonData(String checkinJsonData) {
            this.checkinJsonData = checkinJsonData;
        }
    } //}
    static class CheckinAnwserPutDTO extends CheckinAnwserPostDTO //{
    {
        @NotNull
        private long checkinAnwserId;
        public long getCheckinAnwserId() {
            return this.checkinAnwserId;
        }
        public void setCheckinAnwserId(long checkinAnwserId) {
            this.checkinAnwserId = checkinAnwserId;
        }

        @NotNull
        private String checkinJsonData;
        public String getCheckinJsonData() {
            return this.checkinJsonData;
        }
        public void setCheckinJsonData(String checkinJsonData) {
            this.checkinJsonData = checkinJsonData;
        }
    } //}
    static class CheckinAnwserGetDTO extends CheckinAnwserPutDTO { }
    
    private CheckinAnwserIdResp commitCheckin(CheckinAnwserPostDTO anwser, User user) //{
    {
        final CheckinTask canwser = new CheckinTask();
        ObjUtil.assignFields(canwser, anwser);

        final long checkinAnwserId = this.courseCheckinService.commitCheckin(anwser.getCheckinId(), user, canwser);
        final CheckinAnwserIdResp ans = new CheckinAnwserIdResp();
        ans.setCheckinAnwserId(checkinAnwserId);
        return ans;
    } //}
    static class CheckinAnwserIdResp //{
    {
        private long checkinAnwserId;
        public long getCheckinAnwserId() {
            return this.checkinAnwserId;
        }
        public void setCheckinAnwserId(long checkinAnwserId) {
            this.checkinAnwserId = checkinAnwserId;
        }
    } //}
    @PostMapping()
    private CheckinAnwserIdResp createCheckin(HttpServletRequest httpreq, @RequestBody @Valid CheckinAnwserPostDTO newcheckin) //{
    {
        final User user = DYUtil.getHttpRequestUser(httpreq);
        return this.commitCheckin(newcheckin, user);
    } //}

    @PostMapping("/super")
    private CheckinAnwserIdResp createCheckinSuper(@RequestBody @Valid CheckinAnwserPostDTO newcheckin,
            @RequestParam("userName") String userName) //{
    {
        final User user = this.userService.getUser(userName)
            .orElseThrow(() -> new HttpNotFound("用户不存在"));
        return this.commitCheckin(newcheckin, user);
    } //}


    @DeleteMapping("/nope")
    private void deleteCheckinAnwser(HttpServletRequest httpreq,
            @RequestParam() long checkinAnwserId) //{
    {
        final User user = DYUtil.getHttpRequestUser(httpreq);
        final CheckinTask anwser = this.courseCheckinService.getCheckinTask(checkinAnwserId)
            .orElseThrow(() -> new HttpNotFound("不存在该签到任务的提交"));

        if(!anwser.getStudent().equals(user)) {
            throw new HttpForbidden("不是该任务的提交者");
        }

        this.courseCheckinService.deleteCheckinTask(checkinAnwserId);
    } //}

    @DeleteMapping("/super")
    private void deleteCheckin(@RequestParam() long checkinAnwserId) //{
    {
        this.courseCheckinService.deleteCourseCheckin(checkinAnwserId);
    } //}


    @GetMapping()
    private CheckinAnwserGetDTO getCheckin(HttpServletRequest httpreq,
            @RequestParam() long checkinAnwserId) //{
    {
        final User user = DYUtil.getHttpRequestUser(httpreq);
        final CheckinTask anwser = this.courseCheckinService.getCheckinTask(checkinAnwserId)
            .orElseThrow(() -> new HttpNotFound("任务提交不存在"));

        if(!anwser.getStudent().equals(user) && !anwser.getCourseCheckin().getCourse().getTeacher().equals(user)) {
            throw new HttpForbidden("无权限查看提交的任务");
        }

        return this.getCheckinAnwser(checkinAnwserId);
    } //}
    @GetMapping("/super")
    private CheckinAnwserGetDTO getCheckinAnwser(@RequestParam() long checkinAnwserId) //{
    {
        final CheckinTask anwser = this.courseCheckinService.getCheckinTask(checkinAnwserId)
            .orElseThrow(() -> new HttpNotFound("任务提交不存在"));

        final CheckinAnwserGetDTO ans = new CheckinAnwserGetDTO();
        ObjUtil.assignFields(ans, anwser);

        return ans;
    } //}


    @PutMapping("/nope")
    private void updateCheckinAnwser(HttpServletRequest httpreq, @RequestBody @Valid CheckinAnwserPutDTO uanwser) //{
    {
        final User user = DYUtil.getHttpRequestUser(httpreq);
        final CheckinTask anwser = this.courseCheckinService.getCheckinTask(uanwser.getCheckinAnwserId())
            .orElseThrow(() -> new HttpNotFound("找不到该签到任务的提交"));

        if(!anwser.getStudent().equals(user)) {
            throw new HttpForbidden("无权修改提交");
        }

        this.updateCheckinAnwser(uanwser);
    } //}
    @PutMapping("/super")
    private void updateCheckinAnwser(@RequestBody @Valid CheckinAnwserPutDTO uanwser) //{
    {
        final CheckinTask anwser = this.courseCheckinService.getCheckinTask(uanwser.getCheckinAnwserId())
            .orElseThrow(() -> new HttpNotFound("找不到该任务提交"));

        ObjUtil.assignFields(anwser, uanwser);
        this.courseCheckinService.updateCheckinTask(anwser);
    } //}
}

