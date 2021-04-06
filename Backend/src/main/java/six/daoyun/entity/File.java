package six.daoyun.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import javax.persistence.GeneratedValue;


@Entity
@Table(name="file")
public class File implements Serializable {
	private static final long serialVersionUID = 1L;

    @Id @GeneratedValue @Column(name = "pk_file_id")
    private long id;
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "file_name")
    private String fileNmae;
    public String getFileNmae() {
        return this.fileNmae;
    }
    public void setFileNmae(String fileNmae) {
        this.fileNmae = fileNmae;
    }

    // TODO file blob
    
    private Collection<Role> roles;
    @ManyToMany(mappedBy = "files")
    public Collection<Role> getRoles() {
        return this.roles;
    }
    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }
}

