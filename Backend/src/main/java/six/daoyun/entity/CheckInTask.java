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
@Table(name="tbl_check_in_task")
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

    private String checkInJsonData;
    public String getCheckInJsonData() {
        return this.checkInJsonData;
    }
    public void setCheckInJsonData(String checkInJsonData) {
        this.checkInJsonData = checkInJsonData;
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
    private CourseCheckIn courseCheckIn;
    public CourseCheckIn getCourseCheckIn() {
        return this.courseCheckIn;
    }
    public void setCourseCheckIn(CourseCheckIn courseCheckIn) {
        this.courseCheckIn = courseCheckIn;
    }
}

