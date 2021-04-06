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
@Table(name="course_task")
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

    private Course course;
    @ManyToOne
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

    private Collection<CommitTask> commitedTasks;
    @OneToMany(mappedBy = "course_task")
    public Collection<CommitTask> getCommitedTasks() {
        return this.commitedTasks;
    }
    public void setCommitedTasks(Collection<CommitTask> commitedTasks) {
        this.commitedTasks = commitedTasks;
    }
}

