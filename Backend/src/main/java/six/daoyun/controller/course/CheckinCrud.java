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

import io.swagger.v3.oas.annotations.tags.Tag;
import six.daoyun.controller.DYUtil;
import six.daoyun.controller.exception.HttpForbidden;
import six.daoyun.controller.exception.HttpNotFound;
import six.daoyun.entity.CheckinTask;
import six.daoyun.entity.Course;
import six.daoyun.entity.CourseCheckin;
import six.daoyun.entity.User;
import six.daoyun.service.CourseService;
import six.daoyun.service.CourseCheckinService;
import six.daoyun.utils.ObjUtil;


@Tag(name = "班课签到管理")
@RestController
@RequestMapping("/apis/course/check-in")
public class CheckinCrud {
    @Autowired
    private CourseCheckinService courseCheckinService;
    @Autowired
    private CourseService courseService;

    static class CheckinPostDTO //{
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
        private String jsonData;
        public String getJsonData() {
            return this.jsonData;
        }
        public void setJsonData(String jsonData) {
            this.jsonData = jsonData;
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
    } //}
    static class CheckinPutDTO //{
    {
        @NotNull
        private long checkinId;
        public long getCheckinId() {
            return this.checkinId;
        }
        public void setCheckinId(long checkinId) {
            this.checkinId = checkinId;
        }

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", shape = JsonFormat.Shape.STRING)
        private Date deadline;
        public Date getDeadline() {
            return this.deadline;
        }
        public void setDeadline(Date deadline) {
            this.deadline = deadline;
        }

        private String jsonData;
        public String getJsonData() {
            return this.jsonData;
        }
        public void setJsonData(String jsonData) {
            this.jsonData = jsonData;
        }
    } //}
    static class CheckinGetDTO extends CheckinPutDTO {}

    static class CheckinIdResp //{
    {
        private long checkinId;
        public long getCheckinId() {
            return this.checkinId;
        }
        public void setCheckinId(long checkinId) {
            this.checkinId = checkinId;
        }
    } //}
    private CheckinIdResp createCheckin(CheckinPostDTO newcheckin, User teacher, boolean bypassTeacher) //{
    {
        final Course course = this.courseService.getCourse(newcheckin.getCourseExId())
            .orElseThrow(() -> new HttpNotFound("课程不存在"));

        if(!bypassTeacher && !course.getTeacher().equals(teacher)) {
            throw new HttpForbidden("不是改课程的教师");
        }

        final CourseCheckin ccheckin = new CourseCheckin();
        ObjUtil.assignFields(ccheckin, newcheckin);
        ccheckin.setCourse(course);
        final long checkinId = this.courseCheckinService.createCourseCheckin(ccheckin);
        final CheckinIdResp ans = new CheckinIdResp();
        ans.setCheckinId(checkinId);
        return ans;
    } //}
    @PostMapping()
    private CheckinIdResp createCheckin(HttpServletRequest httpreq, @RequestBody @Valid CheckinPostDTO newcheckin) //{
    {
        final User user = DYUtil.getHttpRequestUser(httpreq);
        return this.createCheckin(newcheckin, user, false);
    } //}

    @PostMapping("/super")
    private CheckinIdResp createCheckinSuper(@RequestBody @Valid CheckinPostDTO newcheckin,
            @RequestParam("userName") String userName) //{
    {
        return this.createCheckin(newcheckin, null, true);
    } //}


    @DeleteMapping()
    private void deleteCheckin(HttpServletRequest httpreq,
            @RequestParam() long checkinId) //{
    {
        final User user = DYUtil.getHttpRequestUser(httpreq);
        final CourseCheckin checkin = this.courseCheckinService.getCourseCheckin(checkinId)
            .orElseThrow(() -> new HttpNotFound("课程签到任务不存在"));

        if(!checkin.getCourse().getTeacher().equals(user)) {
            throw new HttpForbidden("不是该课程教师");
        }

        this.courseCheckinService.deleteCourseCheckin(checkinId);
    } //}

    @DeleteMapping("/super")
    private void deleteCheckin(@RequestParam() long checkinId) //{
    {
        this.courseCheckinService.deleteCourseCheckin(checkinId);
    } //}


    @GetMapping()
    private CheckinGetDTO getCheckin(HttpServletRequest httpreq,
            @RequestParam() long checkinId) //{
    {
        final User user = DYUtil.getHttpRequestUser(httpreq);
        final CourseCheckin checkin = this.courseCheckinService.getCourseCheckin(checkinId)
            .orElseThrow(() -> new HttpNotFound("课程不存在"));

        if(!this.courseService.isMemberOfCourse(checkin.getCourse(), user)) {
            throw new HttpForbidden("不是该课程的成员");
        }

        return this.getCheckin(checkinId);
    } //}
    @GetMapping("/super")
    private CheckinGetDTO getCheckin(@RequestParam() long checkinId) //{
    {
        final CourseCheckin checkin = this.courseCheckinService.getCourseCheckin(checkinId)
            .orElseThrow(() -> new HttpNotFound("课程不存在"));

        final CheckinGetDTO ans = new CheckinGetDTO();
        ObjUtil.assignFields(ans, checkin);

        return ans;
    } //}


    @PutMapping()
    private void updateCheckin(HttpServletRequest httpreq, @RequestBody @Valid CheckinPutDTO ucheckin) //{
    {
        final User user = DYUtil.getHttpRequestUser(httpreq);
        final CourseCheckin checkin = this.courseCheckinService.getCourseCheckin(ucheckin.getCheckinId())
            .orElseThrow(() -> new HttpNotFound("找不到该课程"));

        if(!checkin.getCourse().getTeacher().equals(user)) {
            throw new HttpForbidden("不是该课程的教师");
        }

        this.updateCheckin(ucheckin);
    } //}
    @PutMapping("/super")
    private void updateCheckin(@RequestBody @Valid CheckinPutDTO ucheckin) //{
    {
        final CourseCheckin checkin = this.courseCheckinService.getCourseCheckin(ucheckin.getCheckinId())
            .orElseThrow(() -> new HttpNotFound("找不到该课程"));

        ObjUtil.assignFields(checkin, ucheckin);
        this.courseCheckinService.updateCourseCheckin(checkin);
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
    static class CheckinAnwser //{
    {
        private long checkinAnwserId;
        public long getCheckinAnwserId() {
            return this.checkinAnwserId;
        }
        public void setCheckinAnwserId(long checkinAnwserId) {
            this.checkinAnwserId = checkinAnwserId;
        }

        private String checkinJsonData;
        public String getCheckinJsonData() {
            return this.checkinJsonData;
        }
        public void setCheckinJsonData(String checkinJsonData) {
            this.checkinJsonData = checkinJsonData;
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
    private Collection<CheckinAnwser> getAnwserList(HttpServletRequest httpreq, @RequestParam() long checkinId) //{
    {
        final User user = DYUtil.getHttpRequestUser(httpreq);

        final CourseCheckin checkin = this.courseCheckinService.getCourseCheckin(checkinId)
            .orElseThrow(() -> new HttpNotFound("找不到该签到任务"));

        if(!this.courseService.isMemberOfCourse(checkin.getCourse(), user)) {
            throw new HttpForbidden("不是该课程的成员");
        }

        return this.getAnwserListSuper(checkinId);
    } //}

    @GetMapping("/anwser-list/super")
    private Collection<CheckinAnwser> getAnwserListSuper(@RequestParam() long checkinId) //{
    {
        final CourseCheckin checkin = this.courseCheckinService.getCourseCheckin(checkinId)
            .orElseThrow(() -> new HttpNotFound("找不到该签到任务"));

        final Collection<CheckinAnwser> ans = new ArrayList<>();
        checkin.getChekcInTasks().forEach((anwser) -> {
            StudentInfo s = new StudentInfo(anwser.getStudent());
            CheckinAnwser a = new CheckinAnwser();
            ObjUtil.assignFields(a, anwser);
            a.setCheckinAnwserId(anwser.getId());
            a.setStudentInfo(s);
            ans.add(a);
        });

        return ans;
    } //}

    @GetMapping("/anwser/me")
    private CheckinAnwser getAnwser(HttpServletRequest httpreq, @RequestParam() long checkinId) //{
    {
        final User user = DYUtil.getHttpRequestUser(httpreq);

        final CourseCheckin checkin = this.courseCheckinService.getCourseCheckin(checkinId)
            .orElseThrow(() -> new HttpNotFound("找不到该课程任务"));

        if(!this.courseService.courseHasStudent(checkin.getCourse(), user)) {
            throw new HttpForbidden("不是该课程的学生");
        }

        final CheckinTask anwser = this.courseCheckinService.getCheckinTaskByCheckinAndStudent(checkinId, user)
            .orElseThrow(() -> new HttpNotFound("该签到任务暂时还没有提交记录"));

        final CheckinAnwser ans = new CheckinAnwser();
        ObjUtil.assignFields(ans, anwser);
        ans.setCheckinAnwserId(anwser.getId());
        return ans;
    } //}
}

