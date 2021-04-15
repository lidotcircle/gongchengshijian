package six.daoyun.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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

    @ManyToOne
    private Course course;
    public Course getCourse() {
        return this.course;
    }
    public void setCourse(Course course) {
        this.course = course;
    }

    @Column()
    private String content;
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    @OneToMany(mappedBy = "courseTask")
    private Collection<CommitTask> commitTasks;
    public Collection<CommitTask> getCommitTasks() {
        return this.commitTasks;
    }
    public void setCommitedTasks(Collection<CommitTask> commitTasks) {
        this.commitTasks = commitTasks;
    }
}

