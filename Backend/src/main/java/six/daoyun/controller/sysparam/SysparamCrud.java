package six.daoyun.controller.sysparam;

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
import six.daoyun.entity.SystemParameter;
import six.daoyun.service.SysparamService;

@RestController
class SysparamCrud {

    static class Req //{
    {
        @NotNull
        @Pattern(regexp = "\\w{2,48}", message = "")
        private String key;
        public String getKey() {
            return this.key;
        }
        public void setKey(String key) {
            this.key = key;
        }

        @NotNull
        private String value;
        public String getValue() {
            return this.value;
        }
        public void setValue(String value) {
            this.value = value;
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

        private Iterable<Req> pairs;
        public Iterable<Req> getPairs() {
            return this.pairs;
        }
        public void setPairs(Iterable<Req> pairs) {
            this.pairs = pairs;
        }
    } //}

    @Autowired
    private SysparamService sysparamService;

    @PostMapping("/apis/sysparam")
    private void create(@RequestBody @Valid Req request) {
        SystemParameter param = new SystemParameter();
        param.setParameterName(request.key);
        param.setParameterValue(request.value);
        param.setRemark(request.remark);

        this.sysparamService.create(param);
    }

    @GetMapping("/apis/sysparam")
    private Req getValue(@RequestParam("key") String key) {
        final Req resp = new Req();
        final SystemParameter sp = this.sysparamService.get(key).orElseThrow(() -> new HttpNotFound());
        resp.value = sp.getParameterValue();
        resp.remark = sp.getRemark();
        return resp;
    }

    @PutMapping("/apis/sysparam")
    private void update(@RequestBody @Valid Req request) {
        final SystemParameter sysparam = new SystemParameter();
        sysparam.setParameterName(request.getKey());
        sysparam.setParameterValue(request.getValue());
        sysparam.setRemark(request.getRemark());
        this.sysparamService.update(sysparam);
    }

    @DeleteMapping("/apis/sysparam")
    private void delete(@RequestParam("key") String key) {
        this.sysparamService.delete(key);
    }

    @GetMapping("/apis/sysparam/all-key")
    private Iterable<Req> getKeys() {
        final Collection<Req> ans = new ArrayList<>();
        this.sysparamService.getAll().forEach(v -> {
            Req res = new Req();
            res.key = v.getParameterName();
            res.value = v.getParameterValue();
            ans.add(res);
        });
        return ans;
    }

    @GetMapping("/apis/sysparam/page")
    private PageResp getPage(@RequestParam(value = "pageno", defaultValue = "1") int pageno, 
                             @RequestParam(value = "size", defaultValue = "10") int size, 
                             @RequestParam(name = "sortDir", defaultValue = "parameterName") String sortDir,
                             @RequestParam(name = "sort", defaultValue = "ASC") String sortKey,
                             @RequestParam(name = "searchWildcard", required = false) String filter) {
        final Collection<Req> ans = new ArrayList<>();
        String sortKeyM = "parameterName";
        if(sortKey.equals("value")) {
            sortKeyM = "parameterValue";
        } else if (sortKey.equals("remark")) {
            sortKeyM = "remark";
        }

        Page<SystemParameter> page = this.sysparamService.getAll(pageno - 1, size, sortKeyM, "desc".equalsIgnoreCase(sortDir), filter);
        page.forEach(v -> {
            Req res = new Req();
            res.key = v.getParameterName();
            res.value = v.getParameterValue();
            res.remark = v.getRemark();
            ans.add(res);
        });
        final PageResp resp = new PageResp();
        resp.total = page.getTotalElements();
        resp.pairs = ans;
        return resp;
    }
}

