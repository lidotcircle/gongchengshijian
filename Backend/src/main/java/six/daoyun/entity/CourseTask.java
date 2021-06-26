package six.daoyun.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import javax.persistence.GeneratedValue;


@Entity
@Table(name="tbl_course_task")
public class CourseTask implements Serializable {
	private static final long serialVersionUID = 1L;

    @Id @GeneratedValue @Column(name = "pk_course_task_id")
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
    private Date deadline;
    public Date getDeadline() {
        return this.deadline;
    }
    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    @Column()
    private boolean committable;
    public boolean getCommittable() {
        return this.committable;
    }
    public void setCommittable(boolean committable) {
        this.committable = committable;
    }

    @Column()
    private String taskTitle;
    public String getTaskTitle() {
        return this.taskTitle;
    }
    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    @ManyToOne
    private Course course;
    public Course getCourse() {
        return this.course;
    }
    public void setCourse(Course course) {
        this.course = course;
    }

    @Lob
    @Column()
    private String content;
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    @OneToMany(mappedBy = "courseTask", cascade = CascadeType.REMOVE)
    private Collection<CommitTask> commitTasks;
    public Collection<CommitTask> getCommitTasks() {
        return this.commitTasks;
    }
    public void setCommitedTasks(Collection<CommitTask> commitTasks) {
        this.commitTasks = commitTasks;
    }
}

