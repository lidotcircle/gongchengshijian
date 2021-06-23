package six.daoyun.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


import javax.persistence.GeneratedValue;


@Entity
@Table(name="tbl_role")
public class Role implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue @Column(name = "pk_role_id")
    private long roleId;
    public long getRoleId() {
        return this.roleId;
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

    @Column(name = "uk_role_name", columnDefinition = "VARCHAR(32) NOT NULL")
    private String roleName;
    public String getRoleName() {
        return this.roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @JsonIgnore
    @ManyToMany(mappedBy = "roles")
    private Collection<PermEntry> permEntries;
    public Collection<PermEntry> getPermEntries() {
        return this.permEntries;
    }
    public void setPermEntries(Collection<PermEntry> permEntries) {
        this.permEntries = permEntries;
    }
}

