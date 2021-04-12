package six.daoyun.controller.sysparam;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import six.daoyun.controller.exception.HttpNotFound;
import six.daoyun.entity.SystemParameter;
import six.daoyun.service.SysparamService;

@RestController
class SysparamCrud {

    static class Req {
        @Pattern(regexp = "\\w{2,48}", message = "")
        private String key;
        public String getKey() {
            return this.key;
        }
        public void setKey(String key) {
            this.key = key;
        }

        private String value;
        public String getValue() {
            return this.value;
        }
        public void setValue(String value) {
            this.value = value;
        }
    }

    static class PageReq {
        @NotNull
        @Min(0)
        private Integer pageno;
        public Integer getPageno() {
            return this.pageno;
        }
        public void setPageno(Integer pageno) {
            this.pageno = pageno;
        }
        @NotNull
        @Min(0)
        private Integer size;
        public Integer getSize() {
            return this.size;
        }
        public void setSize(Integer size) {
            this.size = size;
        }
    }

    static class PageResp {
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
    }

    @Autowired
    private SysparamService sysparamService;

    @PostMapping("/apis/sysparam")
    private void create(@RequestBody @Valid Req request) {
        SystemParameter param = new SystemParameter();
        param.setParameterName(request.key);
        param.setParameterValue(request.value);

        this.sysparamService.create(param);
    }

    @GetMapping("/apis/sysparam")
    private Req getValue(@RequestBody @Valid Req request) {
        final Req resp = new Req();
        resp.value = this.sysparamService.get(request.key).orElseThrow(() -> new HttpNotFound());
        return resp;
    }

    @PutMapping("/apis/sysparam")
    private void update(@RequestBody @Valid Req request) {
        this.sysparamService.update(request.key, request.value);
    }

    @DeleteMapping("/apis/sysparam")
    private void delete(@RequestBody @Valid Req request) {
        this.sysparamService.delete(request.key);
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
    private PageResp getPage(@RequestBody @Valid PageReq req) {
        final Collection<Req> ans = new ArrayList<>();
        Page<SystemParameter> page = this.sysparamService.getAll(req.pageno, req.size);
        page.forEach(v -> {
            Req res = new Req();
            res.key = v.getParameterName();
            res.value = v.getParameterValue();
            ans.add(res);
        });
        final PageResp resp = new PageResp();
        resp.total = page.getTotalElements();
        resp.pairs = ans;
        return resp;
    }
}

