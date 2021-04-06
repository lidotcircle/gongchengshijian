package six.daoyun.entity;

import java.util.Collection;
import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import javax.persistence.GeneratedValue;


@Entity
@Table(name="user")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

    @Id @GeneratedValue @Column(name = "pk_user_id")
    private int id;

    @Column(name = "uk_user_name", unique = true)
	private String userName;

    @Column(name = "gmt_created", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private Date createdDate;
    @Column(name = "gmt_modified", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date modifiedDate;

    private Collection<Role> roles;
    @OneToMany
    public Collection<Role> getRoles() {
        return this.roles;
    }
    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    // TODO 头像

    @Column(name = "name", columnDefinition = "VARCHAR(32) NULL")
    private String name;
    @Column(name = "birthday", columnDefinition = "DATE NULL")
    private Date birthday;
    @Column(name = "gender", columnDefinition = "ENUM('unknown', 'male', 'female') DEFAULT 'unknown'")
    private String gender;

    @Column(name = "student_teacher_id", columnDefinition = "VARCHAR(48) NULL")
    private String studentTeacherId;
    @Column(name = "shcool", columnDefinition = "VARCHAR(48) NULL")
    private String school;
    @Column(name = "college", columnDefinition = "VARCHAR(48) NULL")
    private String college;
    @Column(name = "major", columnDefinition = "VARCHAR(48) NULL")
    private String major;

    @Column(name = "idx_phone", columnDefinition = "VARCHAR(20) NULL")
    private String phone;

    // 第三方账号信息
    @Column(name = "third_party_account_type", columnDefinition = "ENUM('none', 'wechat', 'qq') NOT NULL DEFAULT 'none'")
    private String thirdPartyAccountType;
    @Column(name = "third_party_accout", columnDefinition = "VARCHAR(256) NULL DEFAULT NULL")
    private String thirdPartyAccount;

    @Column(name = "password")
    private String password;


    // getter and setter
    
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }
    public Date getModifiedDate() {
        return modifiedDate;
    }

    public String getName() {
        return this.name;
    }
    public void   setName(String name) {
        this.name = name;
    }
    public Date getBirthday() {
        return birthday;
    }
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStudentTeacherId() {
        return studentTeacherId;
    }
    public void setStudentTeacherId(String studentTeacherId) {
        this.studentTeacherId = studentTeacherId;
    }
    public String getSchool() {
        return school;
    }
    public void setSchool(String school) {
        this.school = school;
    }
    public String getCollege() {
        return college;
    }
    public void setCollege(String college) {
        this.college = college;
    }
    public String getMajor() {
        return major;
    }
    public void setMajor(String major) {
        this.major = major;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getThirdPartyAccountType() {
        return thirdPartyAccountType;
    }
    public void setThirdPartyAccountType(String thirdPartyAccountType) {
        this.thirdPartyAccountType = thirdPartyAccountType;
    }
    public String getThirdPartyAccount() {
        return thirdPartyAccount;
    }
    public void setThirdPartyAccount(String thirdPartyAccount) {
        this.thirdPartyAccount = thirdPartyAccount;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}

