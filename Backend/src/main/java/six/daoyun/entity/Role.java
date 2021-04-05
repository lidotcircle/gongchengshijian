package six.daoyun.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import javax.persistence.GeneratedValue;


@Entity
@Table(name="role")
public class Role implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue @Column(name = "pk_role_id")
    private String roleId;
    public String getRoleId() {
        return this.roleId;
    }

    @Column(name = "gmt_created", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private Date createdDate;
    public Date getCreatedDate() {
        return this.createdDate;
    }

    @Column(name = "gmt_modified", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Date modifiedDate;
    public Date getModifiedDate() {
        return this.modifiedDate;
    }

    @Column(name = "uk_role")
    private String roleName;
    public String getRoleName() {
        return this.roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    // TODO role items
}

