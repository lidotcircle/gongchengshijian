package six.daoyun.controller.datadict;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import six.daoyun.controller.exception.HttpNotFound;
import six.daoyun.entity.DictionaryType;
import six.daoyun.service.DataDictionaryService;

@RestController()
class DictType {
    @Autowired
    private DataDictionaryService dictService;

    static class Req //{
    {
        @Pattern(regexp = "[a-zA-Z]\\w{1,32}")
        @NotNull
        private String typeCode;
        public String getTypeCode() {
            return this.typeCode;
        }
        public void setTypeCode(String typeCode) {
            this.typeCode = typeCode;
        }

        @NotNull
        private String typeName;
        public String getTypeName() {
            return this.typeName;
        }
        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        private String remark;
        public String getRemark() {
            return this.remark;
        }
        public void setRemark(String remark) {
            this.remark = remark;
        }
    } //}
    static class PageResp //{
    {
        private long total;
        public long getTotal() {
            return this.total;
        }
        public void setTotal(long total) {
            this.total = total;
        }

        private Collection<Req> pairs;
        public Collection<Req> getPairs() {
            return this.pairs;
        }
        public void setPairs(Collection<Req> pairs) {
            this.pairs = pairs;
        }
    } //}

    @PostMapping("/apis/datadict/type")
    private void createDictType(@RequestBody @Valid Req req) //{
    {
        DictionaryType type = new DictionaryType();
        type.setTypeCode(req.getTypeCode());
        type.setTypeName(req.getTypeName());
        type.setRemark(req.getRemark());

        this.dictService.createDictionaryType(type);
    } //}

    @PutMapping("/apis/datadict/type")
    private void updateDictType(@RequestBody @Valid Req req) //{
    {
        DictionaryType type = this.dictService.getDictionaryType(req.getTypeCode())
            .orElseThrow(() -> new HttpNotFound("数据字典类型'" + req.getTypeCode() + "'不存在"));

        type.setTypeName(req.getTypeName());
        type.setRemark(req.getRemark());
        this.dictService.updateDictionaryType(type);
    } //}

    @GetMapping("/apis/datadict/type")
    private Req getDictType(@RequestParam("typeCode") String typeCode) //{
    {
        Req ans = new Req();
        DictionaryType type = this.dictService.getDictionaryType(typeCode)
            .orElseThrow(() -> new HttpNotFound("数据字典类型'" + typeCode + "'不存在"));

        ans.setTypeCode(type.getTypeCode());
        ans.setTypeName(type.getTypeName());
        ans.setRemark(type.getRemark());

        return ans;
    } //}

    @DeleteMapping("/apis/datadict/type")
    private void deleteDictType(@RequestParam("typeCode") String typeCode) //{
    {
        this.dictService.deleteDictionaryTypeByTypeCode(typeCode);
    } //}

    @GetMapping("/apis/datadict/type/page")
    private PageResp getPage(@RequestParam("pageno") int pageno, 
                             @RequestParam("size") int size, 
                             @RequestParam(value = "sortDir", required = false) String sortDir,
                             @RequestParam(value = "sortKey", defaultValue = "typeName") String sortKey,
                             @RequestParam(value = "searchWildcard", required = false) String wildcard) //{
    {
        PageResp ans = new PageResp();

        Page<DictionaryType> page = 
            this.dictService.getDictionaryTypePage(pageno - 1, size, sortKey, 
                                                   "desc".equalsIgnoreCase(sortDir), wildcard);

        Collection<Req> pairs = new ArrayList<>();
        page.forEach(v -> {
            Req res = new Req();
            res.setTypeCode(v.getTypeCode());
            res.setTypeName(v.getTypeName());
            res.setRemark(v.getRemark());

            pairs.add(res);
        });
        ans.setPairs(pairs);
        ans.setTotal(page.getTotalElements());

        return ans;
    } //}
}

