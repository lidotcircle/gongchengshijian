package six.daoyun.entity;

import java.util.Collection;
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import javax.persistence.GeneratedValue;


@Entity
@Table(name="tbl_dictionary_type")
public class DictionaryType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue @Column(name = "pk_dict_type_id")
    private int dictTypeId;
    public int getDictTypeId() {
        return this.dictTypeId;
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

    @Column(name = "uk_dict_type_name", unique = true, nullable = false)
    private String typeName;
    public String getTypeName() {
        return this.typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Column(name = "type_code", unique = true, nullable = false)
    private String typeCode;
    public String getTypeCode() {
        return this.typeCode;
    }
    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    @OneToMany(mappedBy = "dictionaryType")
    private Collection<DictionaryData> datas;
    public Collection<DictionaryData> getDatas() {
        return this.datas;
    }
    public void setDatas(Collection<DictionaryData> datas) {
        this.datas = datas;
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

