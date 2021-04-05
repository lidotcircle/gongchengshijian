package six.daoyun.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import javax.persistence.GeneratedValue;


@Entity
@Table(name="dictionary_data")
public class DictionaryData implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue @Column(name = "pk_dict_data_id")
    private int dictDataId;
    public int getDictDataId() {
        return this.dictDataId;
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

    // TODO foreign key constrain
    @Column(name = "fk_dict_type_id")
    private int dictTypeId;
    public int getDictTypeId() {
        return this.dictTypeId;
    }
    public void setDictTypeId(int dictTypeId) {
        this.dictTypeId = dictTypeId;
    }

    @Column(name = "keyword")
    private String keyword;
    public String getKeyword() {
        return this.keyword;
    }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Column(name = "value")
    private String value;
    public String getValue() {
        return this.value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    @Column(name = "is_default")
    private boolean isDefault;
    public boolean getIsDefault() {
        return this.isDefault;
    }
    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    @Column(name = "order")
    private int order;
    public int getOrder() {
        return this.order;
    }
    public void setOrder(int order) {
        this.order = order;
    }

    @Column(name = "remark")
    private String remark;
    public String getRemark() {
        return this.remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
}

