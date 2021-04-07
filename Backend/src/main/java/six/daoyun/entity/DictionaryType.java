package six.daoyun.entity;

import java.util.Collection;
import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import javax.persistence.GeneratedValue;


@Entity
@Table(name="dictionary_type")
public class DictionaryType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue @Column(name = "pk_dict_type_id")
    private int dictTypeId;
    public int getDictTypeId() {
        return this.dictTypeId;
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

    @Column(name = "uk_dict_type_name", unique = true)
    private String dictTypeName;
    public String getDictTypeName() {
        return this.dictTypeName;
    }
    public void setDictTypeName(String dictTypeName) {
        this.dictTypeName = dictTypeName;
    }

    @Column(name = "type_code")
    private String typeCOde;
    public String getTypeCOde() {
        return this.typeCOde;
    }
    public void setTypeCOde(String typeCOde) {
        this.typeCOde = typeCOde;
    }

    @OneToMany()
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

