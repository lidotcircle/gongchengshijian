package six.daoyun.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import javax.persistence.GeneratedValue;


@Entity
@Table(name="tbl_commit_task")
public class CommitTask implements Serializable {
	private static final long serialVersionUID = 1L;

    @Id @GeneratedValue @Column(name = "pk_commit_task_id")
    private long id;
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "gmt_created", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP", insertable = false, updatable = false)
    private Date createdDate;
    public Date getCreatedDate() {
        return this.createdDate;
    }
    @Column(name = "gmt_modified", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP", insertable = false, updatable = false)
    private Date modifiedDate;
    public Date getModifiedDate() {
        return this.modifiedDate;
    }

    @Column()
    private String commitContent;
    public String getCommitContent() {
        return this.commitContent;
    }
    public void setCommitContent(String commitContent) {
        this.commitContent = commitContent;
    }

    @Column()
    private String teacherDoThis;
    public String getTeacherDoThis() {
        return this.teacherDoThis;
    }
    public void setTeacherDoThis(String teacherDoThis) {
        this.teacherDoThis = teacherDoThis;
    }

    @Column()
    private long grade;
    public long getGrade() {
        return this.grade;
    }
    public void setGrade(long grade) {
        this.grade = grade;
    }

    @ManyToOne()
    private User student;
    public User getStudent() {
        return this.student;
    }
    public void setStudent(User student) {
        this.student = student;
    }

    @ManyToOne()
    private CourseTask courseTask;
    public CourseTask getCourseTask() {
        return this.courseTask;
    }
    public void setCourse(CourseTask courseTask) {
        this.courseTask = courseTask;
    }
}

