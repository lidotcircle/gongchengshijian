package six.daoyun.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import javax.persistence.GeneratedValue;


@Entity
@Table(name="check_in_task")
public class CheckInTask implements Serializable {
	private static final long serialVersionUID = 1L;

    @Id @GeneratedValue @Column(name = "pk_checkin_task_id")
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

    @ManyToMany
    private Collection<User> checkedStudents;
    public Collection<User> getCheckedStudents() {
        return this.checkedStudents;
    }
    public void setCheckedStudents(Collection<User> checkedStudents) {
        this.checkedStudents = checkedStudents;
    }
}

