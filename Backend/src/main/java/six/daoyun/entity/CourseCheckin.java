package six.daoyun.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import javax.persistence.GeneratedValue;


@Entity
@Table(name="tbl_course_check_in")
public class CourseCheckin implements Serializable {
	private static final long serialVersionUID = 1L;

    @Id @GeneratedValue @Column(name = "pk_course_check_in_id")
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

    private String jsonData;
    public String getJsonData() {
        return this.jsonData;
    }
    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    @ManyToOne
    private Course course;
    public Course getCourse() {
        return this.course;
    }
    public void setCourse(Course course) {
        this.course = course;
    }

    @OneToMany(mappedBy = "courseCheckin")
    private Collection<CheckinTask> chekcInTasks;
    public Collection<CheckinTask> getChekcInTasks() {
        return this.chekcInTasks;
    }
    public void setChekcInTasks(Collection<CheckinTask> chekcInTasks) {
        this.chekcInTasks = chekcInTasks;
    }
}

