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

import six.daoyun.controller.DYUtil;
import six.daoyun.controller.exception.HttpForbidden;
import six.daoyun.controller.exception.HttpNotFound;
import six.daoyun.entity.CheckInTask;
import six.daoyun.entity.User;
import six.daoyun.service.CourseCheckInService;
import six.daoyun.service.UserService;
import six.daoyun.utils.ObjUtil;

@RestController
@RequestMapping("/apis/course/check-in/anwser")
public class CheckInCommitCrud {
    @Autowired
    private CourseCheckInService courseCheckInService;
    @Autowired
    private UserService userService;

    static class CheckInAnwserPostDTO //{
    {
        @NotNull
        private long checkInId;
        public long getCheckInId() {
            return this.checkInId;
        }
        public void setCheckInId(long checkInId) {
            this.checkInId = checkInId;
        }

        @NotNull
        private String checkInJsonData;
        public String getCheckInJsonData() {
            return this.checkInJsonData;
        }
        public void setCheckInJsonData(String checkInJsonData) {
            this.checkInJsonData = checkInJsonData;
        }
    } //}
    static class CheckInAnwserPutDTO extends CheckInAnwserPostDTO //{
    {
        @NotNull
        private long checkInAnwserId;
        public long getCheckInAnwserId() {
            return this.checkInAnwserId;
        }
        public void setCheckInAnwserId(long checkInAnwserId) {
            this.checkInAnwserId = checkInAnwserId;
        }

        @NotNull
        private String checkInJsonData;
        public String getCheckInJsonData() {
            return this.checkInJsonData;
        }
        public void setCheckInJsonData(String checkInJsonData) {
            this.checkInJsonData = checkInJsonData;
        }
    } //}
    static class CheckInAnwserGetDTO extends CheckInAnwserPutDTO { }
    
    private CheckInAnwserIdResp commitCheckIn(CheckInAnwserPostDTO anwser, User user) //{
    {
        final CheckInTask canwser = new CheckInTask();
        ObjUtil.assignFields(canwser, anwser);

        final long checkInAnwserId = this.courseCheckInService.commitCheckIn(anwser.getCheckInId(), user, canwser);
        final CheckInAnwserIdResp ans = new CheckInAnwserIdResp();
        ans.setCheckInAnwserId(checkInAnwserId);
        return ans;
    } //}
    static class CheckInAnwserIdResp //{
    {
        private long checkInAnwserId;
        public long getCheckInAnwserId() {
            return this.checkInAnwserId;
        }
        public void setCheckInAnwserId(long checkInAnwserId) {
            this.checkInAnwserId = checkInAnwserId;
        }
    } //}
    @PostMapping()
    private CheckInAnwserIdResp createCheckIn(HttpServletRequest httpreq, @RequestBody @Valid CheckInAnwserPostDTO newcheckIn) //{
    {
        final User user = DYUtil.getHttpRequestUser(httpreq);
        return this.commitCheckIn(newcheckIn, user);
    } //}

    @PostMapping("/super")
    private CheckInAnwserIdResp createCheckInSuper(@RequestBody @Valid CheckInAnwserPostDTO newcheckIn,
            @RequestParam("userName") String userName) //{
    {
        final User user = this.userService.getUser(userName)
            .orElseThrow(() -> new HttpNotFound("用户不存在"));
        return this.commitCheckIn(newcheckIn, user);
    } //}


    @DeleteMapping("/nope")
    private void deleteCheckInAnwser(HttpServletRequest httpreq,
            @RequestParam() long checkInAnwserId) //{
    {
        final User user = DYUtil.getHttpRequestUser(httpreq);
        final CheckInTask anwser = this.courseCheckInService.getCheckInTask(checkInAnwserId)
            .orElseThrow(() -> new HttpNotFound("不存在该签到任务的提交"));

        if(!anwser.getStudent().equals(user)) {
            throw new HttpForbidden("不是该任务的提交者");
        }

        this.courseCheckInService.deleteCheckInTask(checkInAnwserId);
    } //}

    @DeleteMapping("/super")
    private void deleteCheckIn(@RequestParam() long checkInAnwserId) //{
    {
        this.courseCheckInService.deleteCourseCheckIn(checkInAnwserId);
    } //}


    @GetMapping()
    private CheckInAnwserGetDTO getCheckIn(HttpServletRequest httpreq,
            @RequestParam() long checkInAnwserId) //{
    {
        final User user = DYUtil.getHttpRequestUser(httpreq);
        final CheckInTask anwser = this.courseCheckInService.getCheckInTask(checkInAnwserId)
            .orElseThrow(() -> new HttpNotFound("任务提交不存在"));

        if(!anwser.getStudent().equals(user) && !anwser.getCourseCheckIn().getCourse().getTeacher().equals(user)) {
            throw new HttpForbidden("无权限查看提交的任务");
        }

        return this.getCheckInAnwser(checkInAnwserId);
    } //}
    @GetMapping("/super")
    private CheckInAnwserGetDTO getCheckInAnwser(@RequestParam() long checkInAnwserId) //{
    {
        final CheckInTask anwser = this.courseCheckInService.getCheckInTask(checkInAnwserId)
            .orElseThrow(() -> new HttpNotFound("任务提交不存在"));

        final CheckInAnwserGetDTO ans = new CheckInAnwserGetDTO();
        ObjUtil.assignFields(ans, anwser);

        return ans;
    } //}


    @PutMapping("/nope")
    private void updateCheckInAnwser(HttpServletRequest httpreq, @RequestBody @Valid CheckInAnwserPutDTO uanwser) //{
    {
        final User user = DYUtil.getHttpRequestUser(httpreq);
        final CheckInTask anwser = this.courseCheckInService.getCheckInTask(uanwser.getCheckInAnwserId())
            .orElseThrow(() -> new HttpNotFound("找不到该签到任务的提交"));

        if(!anwser.getStudent().equals(user)) {
            throw new HttpForbidden("无权修改提交");
        }

        this.updateCheckInAnwser(uanwser);
    } //}
    @PutMapping("/super")
    private void updateCheckInAnwser(@RequestBody @Valid CheckInAnwserPutDTO uanwser) //{
    {
        final CheckInTask anwser = this.courseCheckInService.getCheckInTask(uanwser.getCheckInAnwserId())
            .orElseThrow(() -> new HttpNotFound("找不到该任务提交"));

        ObjUtil.assignFields(anwser, uanwser);
        this.courseCheckInService.updateCheckInTask(anwser);
    } //}
}

