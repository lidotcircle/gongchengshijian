package six.daoyun.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import javax.persistence.GeneratedValue;


@Entity
@Table(name="menu")
public class Menu implements Serializable {
	private static final long serialVersionUID = 1L;

    @Id @GeneratedValue @Column(name = "pk_menu_id")
    private long id;
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "menu_name")
    private String menuName;
    public String getMenuName() {
        return this.menuName;
    }
    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    @ManyToOne
    private Menu parent;
    public Menu getParent() {
        return this.parent;
    }
    public void setParent(Menu parent) {
        this.parent = parent;
    }

    private Collection<Menu> children;
    @OneToMany(mappedBy = "parent")
    public Collection<Menu> getChildren() {
        return this.children;
    }
    public void setChildren(Collection<Menu> children) {
        this.children = children;
    }

    private Collection<Role> roles;
    @ManyToMany(mappedBy = "menus")
    public Collection<Role> getRoles() {
        return this.roles;
    }
    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }
}

