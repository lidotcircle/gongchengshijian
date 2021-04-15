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
@Table(name="tbl_perm_entry")
public class PermEntry implements Serializable {
	private static final long serialVersionUID = 1L;

    @Id @GeneratedValue @Column(name = "pk_perm_entry_id")
    private long id;
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "perm_entry_name")
    private String perm_entryName;
    public String getMenuName() {
        return this.perm_entryName;
    }
    public void setMenuName(String perm_entryName) {
        this.perm_entryName = perm_entryName;
    }

    @Column(name = "uk_link")
    private String link;
    public String getLink() {
        return this.link;
    }
    public void setLink(String link) {
        this.link = link;
    }

    @Column(name = "entry_type", columnDefinition = "ENUM('menu', 'page', 'button') DEFAULT 'menu'")
    private String entryType;
    public String getEntryType() {
        return this.entryType;
    }
    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    @ManyToOne()
    private PermEntry parent;
    public PermEntry getParent() {
        return this.parent;
    }
    public void setParent(PermEntry parent) {
        this.parent = parent;
    }

    @OneToMany(mappedBy = "parent")
    private Collection<PermEntry> children;
    public Collection<PermEntry> getChildren() {
        return children;
    }
    public void setChildren(Collection<PermEntry> children) {
        this.children = children;
    }

    @ManyToMany()
    private Collection<Role> roles;
    public Collection<Role> getRoles() {
        return this.roles;
    }
    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }
}

