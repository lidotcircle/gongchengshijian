package six.daoyun.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import javax.persistence.GeneratedValue;


@Entity
@Table(name="tbl_system_parameter")
public class SystemParameter implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue @Column(name = "pk_id")
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

    @Column(name = "uk_key", columnDefinition = "VARCHAR(48) NOT NULL UNIQUE")
    private String parameterName;
    public String getParameterName() {
        return this.parameterName;
    }
    public void setParameterName(String parameterName) {
        this.parameterName = parameterName;
    }

    @Column(name = "value")
    private String parameterValue;
    public String getParameterValue() {
        return this.parameterValue;
    }
    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }

    @Lob
    private String remark;
    public String getRemark() {
        return this.remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
}

