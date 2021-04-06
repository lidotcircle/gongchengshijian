package six.daoyun.entity;

import java.io.Serializable;
import java.util.Collection;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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

    @Column(name = "uk_role_name")
    private String roleName;
    public String getRoleName() {
        return this.roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    private Collection<Menu> menus;
    @ManyToMany()
    public Collection<Menu> getMenus() {
        return this.menus;
    }
    public void setMenus(Collection<Menu> menus) {
        this.menus = menus;
    }

    private Collection<Button> buttons;
    @ManyToMany()
    public Collection<Button> getButtons() {
        return this.buttons;
    }
    public void setButtons(Collection<Button> buttons) {
        this.buttons = buttons;
    }

    private Collection<File> files;
    @ManyToMany()
    public Collection<File> getFiles() {
        return this.files;
    }
    public void setFiles(Collection<File> files) {
        this.files = files;
    }
}

