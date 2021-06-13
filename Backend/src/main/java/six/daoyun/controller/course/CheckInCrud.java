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
import six.daoyun.entity.CheckInTask;
import six.daoyun.entity.Course;
import six.daoyun.entity.CourseCheckIn;
import six.daoyun.entity.User;
import six.daoyun.service.CourseService;
import six.daoyun.service.CourseCheckInService;
import six.daoyun.utils.ObjUtil;

@RestController
@RequestMapping("/apis/course/check-in")
public class CheckInCrud {
    @Autowired
    private CourseCheckInService courseCheckInService;
    @Autowired
    private CourseService courseService;

    static class CheckInPostDTO //{
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
    static class CheckInPutDTO //{
    {
        @NotNull
        private long checkInId;
        public long getCheckInId() {
            return this.checkInId;
        }
        public void setCheckInId(long checkInId) {
            this.checkInId = checkInId;
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
    static class CheckInGetDTO extends CheckInPutDTO {}

    static class CheckInIdResp //{
    {
        private long checkInId;
        public long getCheckInId() {
            return this.checkInId;
        }
        public void setCheckInId(long checkInId) {
            this.checkInId = checkInId;
        }
    } //}
    private CheckInIdResp createCheckIn(CheckInPostDTO newcheckIn, User teacher, boolean bypassTeacher) //{
    {
        final Course course = this.courseService.getCourse(newcheckIn.getCourseExId())
            .orElseThrow(() -> new HttpNotFound("课程不存在"));

        if(!bypassTeacher && !course.getTeacher().equals(teacher)) {
            throw new HttpForbidden("不是改课程的教师");
        }

        final CourseCheckIn ccheckIn = new CourseCheckIn();
        ObjUtil.assignFields(ccheckIn, newcheckIn);
        ccheckIn.setCourse(course);
        final long checkInId = this.courseCheckInService.createCourseCheckIn(ccheckIn);
        final CheckInIdResp ans = new CheckInIdResp();
        ans.setCheckInId(checkInId);
        return ans;
    } //}
    @PostMapping()
    private CheckInIdResp createCheckIn(HttpServletRequest httpreq, @RequestBody @Valid CheckInPostDTO newcheckIn) //{
    {
        final User user = DYUtil.getHttpRequestUser(httpreq);
        return this.createCheckIn(newcheckIn, user, false);
    } //}

    @PostMapping("/super")
    private CheckInIdResp createCheckInSuper(@RequestBody @Valid CheckInPostDTO newcheckIn,
            @RequestParam("userName") String userName) //{
    {
        return this.createCheckIn(newcheckIn, null, true);
    } //}


    @DeleteMapping()
    private void deleteCheckIn(HttpServletRequest httpreq,
            @RequestParam() long checkInId) //{
    {
        final User user = DYUtil.getHttpRequestUser(httpreq);
        final CourseCheckIn checkIn = this.courseCheckInService.getCourseCheckIn(checkInId)
            .orElseThrow(() -> new HttpNotFound("课程签到任务不存在"));

        if(!checkIn.getCourse().getTeacher().equals(user)) {
            throw new HttpForbidden("不是该课程教师");
        }

        this.courseCheckInService.deleteCourseCheckIn(checkInId);
    } //}

    @DeleteMapping("/super")
    private void deleteCheckIn(@RequestParam() long checkInId) //{
    {
        this.courseCheckInService.deleteCourseCheckIn(checkInId);
    } //}


    @GetMapping()
    private CheckInGetDTO getCheckIn(HttpServletRequest httpreq,
            @RequestParam() long checkInId) //{
    {
        final User user = DYUtil.getHttpRequestUser(httpreq);
        final CourseCheckIn checkIn = this.courseCheckInService.getCourseCheckIn(checkInId)
            .orElseThrow(() -> new HttpNotFound("课程不存在"));

        if(!this.courseService.isMemberOfCourse(checkIn.getCourse(), user)) {
            throw new HttpForbidden("不是该课程的成员");
        }

        return this.getCheckIn(checkInId);
    } //}
    @GetMapping("/super")
    private CheckInGetDTO getCheckIn(@RequestParam() long checkInId) //{
    {
        final CourseCheckIn checkIn = this.courseCheckInService.getCourseCheckIn(checkInId)
            .orElseThrow(() -> new HttpNotFound("课程不存在"));

        final CheckInGetDTO ans = new CheckInGetDTO();
        ObjUtil.assignFields(ans, checkIn);

        return ans;
    } //}


    @PutMapping()
    private void updateCheckIn(HttpServletRequest httpreq, @RequestBody @Valid CheckInPutDTO ucheckIn) //{
    {
        final User user = DYUtil.getHttpRequestUser(httpreq);
        final CourseCheckIn checkIn = this.courseCheckInService.getCourseCheckIn(ucheckIn.getCheckInId())
            .orElseThrow(() -> new HttpNotFound("找不到该课程"));

        if(!checkIn.getCourse().getTeacher().equals(user)) {
            throw new HttpForbidden("不是该课程的教师");
        }

        this.updateCheckIn(ucheckIn);
    } //}
    @PutMapping("/super")
    private void updateCheckIn(@RequestBody @Valid CheckInPutDTO ucheckIn) //{
    {
        final CourseCheckIn checkIn = this.courseCheckInService.getCourseCheckIn(ucheckIn.getCheckInId())
            .orElseThrow(() -> new HttpNotFound("找不到该课程"));

        ObjUtil.assignFields(checkIn, ucheckIn);
        this.courseCheckInService.updateCourseCheckIn(checkIn);
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
    static class CheckInAnwser //{
    {
        private long checkInAnwserId;
        public long getCheckInAnwserId() {
            return this.checkInAnwserId;
        }
        public void setCheckInAnwserId(long checkInAnwserId) {
            this.checkInAnwserId = checkInAnwserId;
        }

        private String checkInJsonData;
        public String getCheckInJsonData() {
            return this.checkInJsonData;
        }
        public void setCheckInJsonData(String checkInJsonData) {
            this.checkInJsonData = checkInJsonData;
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
    private Collection<CheckInAnwser> getAnwserList(HttpServletRequest httpreq, @RequestParam() long checkInId) //{
    {
        final User user = DYUtil.getHttpRequestUser(httpreq);

        final CourseCheckIn checkIn = this.courseCheckInService.getCourseCheckIn(checkInId)
            .orElseThrow(() -> new HttpNotFound("找不到该签到任务"));

        if(!this.courseService.isMemberOfCourse(checkIn.getCourse(), user)) {
            throw new HttpForbidden("不是该课程的成员");
        }

        return this.getAnwserListSuper(checkInId);
    } //}

    @GetMapping("/anwser-list/super")
    private Collection<CheckInAnwser> getAnwserListSuper(@RequestParam() long checkInId) //{
    {
        final CourseCheckIn checkIn = this.courseCheckInService.getCourseCheckIn(checkInId)
            .orElseThrow(() -> new HttpNotFound("找不到该签到任务"));

        final Collection<CheckInAnwser> ans = new ArrayList<>();
        checkIn.getChekcInTasks().forEach((anwser) -> {
            StudentInfo s = new StudentInfo(anwser.getStudent());
            CheckInAnwser a = new CheckInAnwser();
            ObjUtil.assignFields(a, anwser);
            a.setCheckInAnwserId(anwser.getId());
            a.setStudentInfo(s);
            ans.add(a);
        });

        return ans;
    } //}

    @GetMapping("/anwser")
    private CheckInAnwser getAnwser(HttpServletRequest httpreq, @RequestParam() long checkInId) //{
    {
        final User user = DYUtil.getHttpRequestUser(httpreq);

        final CourseCheckIn checkIn = this.courseCheckInService.getCourseCheckIn(checkInId)
            .orElseThrow(() -> new HttpNotFound("找不到该课程任务"));

        if(!this.courseService.courseHasStudent(checkIn.getCourse(), user)) {
            throw new HttpForbidden("不是该课程的学生");
        }

        final CheckInTask anwser = this.courseCheckInService.getCheckInTaskByCheckInAndStudent(checkInId, user)
            .orElseThrow(() -> new HttpNotFound("该签到任务暂时还没有提交记录"));

        final CheckInAnwser ans = new CheckInAnwser();
        ObjUtil.assignFields(ans, anwser);
        ans.setCheckInAnwserId(anwser.getId());
        return ans;
    } //}
}

