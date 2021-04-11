package six.daoyun.exchange;


public class UserUnprivileged {
    private String name;
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }

    private long birthday = -1;
    public long getBirthday() {
        return this.birthday;
    }
    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    private String gender;
    public String getGender() {
        return this.gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    private String studentTeacherId;
    public String getStudentTeacherId() {
        return this.studentTeacherId;
    }
    public void setStudentTeacherId(String studentTeacherId) {
        this.studentTeacherId = studentTeacherId;
    }

    private String school;
    public String getSchool() {
        return this.school;
    }
    public void setSchool(String school) {
        this.school = school;
    }

    private String college;
    public String getCollege() {
        return this.college;
    }
    public void setCollege(String college) {
        this.college = college;
    }

    private String major;
    public String getMajor() {
        return this.major;
    }
    public void setMajor(String major) {
        this.major = major;
    }
}

