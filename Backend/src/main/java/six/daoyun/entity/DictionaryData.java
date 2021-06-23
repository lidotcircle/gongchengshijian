package six.daoyun.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import javax.persistence.GeneratedValue;


@Entity
@Table(name="tbl_dictionary_data")
public class DictionaryData implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue @Column(name = "pk_dict_data_id")
    private int dictDataId;
    public int getDictDataId() {
        return this.dictDataId;
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

    @ManyToOne()
    private DictionaryType dictionaryType;
    public DictionaryType getDictionaryType() {
        return this.dictionaryType;
    }
    public void setDictionaryType(DictionaryType dictionaryType) {
        this.dictionaryType = dictionaryType;
    }

    @Column(name = "keyword", nullable = false)
    private String keyword;
    public String getKeyword() {
        return this.keyword;
    }
    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    @Column(name = "data_value", nullable = false)
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

    @Column(name = "data_order")
    private int order;
    public int getOrder() {
        return this.order;
    }
    public void setOrder(int order) {
        this.order = order;
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

