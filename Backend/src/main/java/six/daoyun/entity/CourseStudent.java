package six.daoyun.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_course_student")
@IdClass(CourseStudent.CourseStudentId.class)
public class CourseStudent {
    @Embeddable
    public static class CourseStudentId implements Serializable //{
    {
		private static final long serialVersionUID = -8770060760906437516L;

        private int student;
        public int getStudent() {
            return this.student;
        }
        public void setStudent(int student) {
            this.student = student;
        }

        private long course;
        public long getCourse() {
            return this.course;
        }
        public void setCourse(long course) {
            this.course = course;
        }

        @Override
        public boolean equals(Object uc) {
            if(!(uc instanceof CourseStudentId)) {
                return false;
            }
            final CourseStudentId uuc = (CourseStudentId) uc;
            return this.student == uuc.student && this.course == uuc.course;
        }

        @Override
        public int hashCode() {
            return (int)(this.student + (this.course << 16));
        }
    } //}

    @Id
    @ManyToOne
    @JoinColumn(name = "student_id", referencedColumnName = "pk_user_id")
    private User student;
    public User getStudent() {
        return this.student;
    }
    public void setStudent(User student) {
        this.student = student;
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "pk_course_id")
    private Course course;
    public Course getCourse() {
        return this.course;
    }
    public void setCourse(Course course) {
        this.course = course;
    }

    @Column()
    private long grade;
    public long getGrade() {
        return this.grade;
    }
    public void setGrade(long grade) {
        this.grade = grade;
    }
}

