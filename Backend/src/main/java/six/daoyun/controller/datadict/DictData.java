package six.daoyun.controller.datadict;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
import six.daoyun.entity.DictionaryData;
import six.daoyun.entity.DictionaryType;
import six.daoyun.service.DataDictionaryService;

@RestController()
class DictData {
    @Autowired
    private DataDictionaryService dataService;

    static class Req //{
    {
        @NotNull
        private String typeCode;
        public String getTypeCode() {
            return this.typeCode;
        }
        public void setTypeCode(String typeCode) {
            this.typeCode = typeCode;
        }

        @NotNull
        private String keyword;
        public String getKeyword() {
            return this.keyword;
        }
        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        @NotNull
        private String value;
        public String getValue() {
            return this.value;
        }
        public void setValue(String value) {
            this.value = value;
        }

        private boolean isDefault;
        public boolean getIsDefault() {
            return this.isDefault;
        }
        public void setIsDefault(boolean isDefault) {
            this.isDefault = isDefault;
        }

        private int order;
        public int getOrder() {
            return this.order;
        }
        public void setOrder(int order) {
            this.order = order;
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

    @PostMapping("/apis/datadict/data")
    private void createDictData(@RequestBody @Valid Req req) //{
    {
        DictionaryData data = new DictionaryData();
        DictionaryType type = this.dataService.getDictionaryType(req.getTypeCode())
            .orElseThrow(() -> new HttpNotFound("数据字典类型'" + req.getTypeCode() + "'不存在"));

        data.setDictionaryType(type);
        data.setKeyword(req.getKeyword());
        data.setValue(req.getValue());
        data.setRemark(req.getRemark());
        data.setOrder(req.getOrder());
        data.setIsDefault(req.getIsDefault());

        this.dataService.createDictionaryData(data);
    } //}

    @PutMapping("/apis/datadict/data")
    private void updateDictData(@RequestBody @Valid Req req) //{
    {
        DictionaryType type = this.dataService.getDictionaryType(req.getTypeCode())
            .orElseThrow(() -> new HttpNotFound("数据字典类型'" + req.getTypeCode() + "'不存在"));
        DictionaryData data = this.dataService.getDictionaryData(type, req.getKeyword())
            .orElseThrow(() -> new HttpNotFound("数据不存在"));

        data.setOrder(req.getOrder());
        data.setValue(req.getValue());
        data.setIsDefault(req.getIsDefault());

        this.dataService.updateDictionaryData(data);
    } //}

    @DeleteMapping("/apis/datadict/data")
    private void updateDictData(@RequestParam("typeCode") String typeCode, 
                                @RequestParam("keyword") String keyword) //{
    {
        DictionaryType type = this.dataService.getDictionaryType(typeCode)
            .orElseThrow(() -> new HttpNotFound("数据字典类型'" + typeCode + "'不存在"));
        this.dataService.deleteDictionaryData(type, keyword);
    } //}

    @GetMapping("/apis/datadict/data")
    private Req getDictData(@RequestParam("typeCode") String typeCode, 
                                @RequestParam("keyword") String keyword) //{
    {
        DictionaryType type = this.dataService.getDictionaryType(typeCode)
            .orElseThrow(() -> new HttpNotFound("数据字典类型'" + typeCode + "'不存在"));
        DictionaryData data = this.dataService.getDictionaryData(type, keyword)
            .orElseThrow(() -> new HttpNotFound("数据字典数据不存在"));

        Req ans = new Req();
        ans.setTypeCode(type.getTypeCode());
        ans.setOrder(data.getOrder());
        ans.setValue(data.getValue());
        ans.setRemark(data.getRemark());
        ans.setKeyword(data.getKeyword());
        ans.setIsDefault(data.getIsDefault());
        return ans;
    } //}

    @GetMapping("/apis/datadict/data/page")
    private PageResp getPage(@RequestParam(value = "pageno", defaultValue = "1") int pageno, 
                             @RequestParam(value = "size", defaultValue = "10") int size, 
                             @RequestParam("typeCode") String typeCode,
                             @RequestParam(value = "sortDir", required = false) String sortDir,
                             @RequestParam(value = "sortKey", defaultValue = "keyword") String sortKey,
                             @RequestParam(value = "searchWildcard", required = false) String wildcard) //{
    {
        DictionaryType type = this.dataService.getDictionaryType(typeCode)
            .orElseThrow(() -> new HttpNotFound("数据字典类型'" + typeCode + "'不存在"));

        PageResp ans = new PageResp();
        String sortKeyM = sortKey;
        if("value".equals(sortKey)) {
            sortKeyM = sortKey;
        } else if ("remark".equals(sortKey)) {
            sortKeyM = sortKey;
        }

        Page<DictionaryData> page = 
            this.dataService.getDictionaryDataPage(type, pageno - 1, size, sortKeyM, 
                                                   "desc".equalsIgnoreCase(sortDir), wildcard);
        Collection<Req> pairs = new ArrayList<>();
        page.forEach(v -> {
            Req res = new Req();
            res.setTypeCode(typeCode);
            res.setOrder(v.getOrder());
            res.setValue(v.getValue());
            res.setKeyword(v.getKeyword());
            res.setRemark(v.getRemark());
            res.setIsDefault(v.getIsDefault());

            pairs.add(res);
        });
        ans.setPairs(pairs);
        ans.setTotal(page.getTotalElements());

        return ans;
    } //}

}

