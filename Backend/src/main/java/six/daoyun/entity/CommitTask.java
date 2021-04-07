package six.daoyun.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import javax.persistence.GeneratedValue;


@Entity
@Table(name="commit_task")
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

    @Column()
    private String commitContent;
    public String getCommitContent() {
        return this.commitContent;
    }
    public void setCommitContent(String commitContent) {
        this.commitContent = commitContent;
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

