package six.daoyun.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import javax.persistence.GeneratedValue;


@Entity
@Table(name="tbl_course")
public class Course implements Serializable {
	private static final long serialVersionUID = 1L;

    @Id @GeneratedValue @Column(name = "pk_course_id")
    private long id;
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Column(unique = true)
    private String courseExId;
    public String getCourseExId() {
        return this.courseExId;
    }
    public void setCourseExId(String courseExId) {
        this.courseExId = courseExId;
    }

    @Column(name = "course_name")
    private String courseName;
    public String getCourseName() {
        return this.courseName;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    @Column()
    private String briefDescription;
    public String getBriefDescription() {
        return this.briefDescription;
    }
    public void setBriefDescription(String briefDescription) {
        this.briefDescription = briefDescription;
    }

    @ManyToOne()
    private User teacher;
    public User getTeacher() {
        return this.teacher;
    }
    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    @OneToMany(mappedBy = "course")
    private Collection<CourseStudent> students;
    public Collection<CourseStudent> getStudents() {
        return this.students;
    }
    public void setStudents(Collection<CourseStudent> students) {
        this.students = students;
    }

    public Collection<User> getTrueStudents() {
        Collection<User> ans = new ArrayList<>();
        this.getStudents().forEach(val -> ans.add(val.getStudent()));
        return ans;
    }

    @OneToMany(mappedBy = "course")
    private Collection<CheckInTask> checkInTask;
    public Collection<CheckInTask> getCheckInTask() {
        return this.checkInTask;
    }
    public void setCheckInTask(Collection<CheckInTask> checkInTask) {
        this.checkInTask = checkInTask;
    }

    @OneToMany(mappedBy = "course")
    private Collection<CourseTask> tasks;
    public Collection<CourseTask> getTasks() {
        return this.tasks;
    }
    public void setTasks(Collection<CourseTask> tasks) {
        this.tasks = tasks;
    }
}

