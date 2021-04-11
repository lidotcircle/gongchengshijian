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
@Table(name="tbl_refresh_token")
public class RefreshToken implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue @Column(name = "token_id")
    private long id;
    public long getId() {
        return this.id;
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

    @Column(name = "uk_token", columnDefinition = "VARCHAR(48) NOT NULL UNIQUE")
    private String token;
    public String getToken() {
        return this.token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    @Column()
    private Date expiredDate;
    public Date getExpiredDate() {
        return this.expiredDate;
    }
    public void setExpiredDate(Date expiredDate) {
        this.expiredDate = expiredDate;
    }

    @ManyToOne()
    private User user;
    public User getUser() {
        return this.user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}

