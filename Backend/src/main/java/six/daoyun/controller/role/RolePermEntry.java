package six.daoyun.controller.role;

import java.util.Collection;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import six.daoyun.controller.exception.HttpNotFound;
import six.daoyun.service.RoleService;


@Tag(name = "角色权限配置")
@RestController
@RequestMapping("/apis/role/perm")
public class RolePermEntry {
    @Autowired
    private RoleService roleService;

    static class PermEntryReqDTO //{
    {
        @NotNull
        private String descriptor;
        public String getDescriptor() {
            return this.descriptor;
        }
        public void setDescriptor(String descriptor) {
            this.descriptor = descriptor;
        }

        @NotNull
        private String roleName;
        public String getRoleName() {
            return this.roleName;
        }
        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        private boolean recursive;
        public boolean getRecursive() {
            return this.recursive;
        }
        public void setRecursive(boolean recursive) {
            this.recursive = recursive;
        }
    } //}
    @PostMapping()
    private void enablePerm(@RequestBody @Valid PermEntryReqDTO req) //{
    {
        if(req.getRecursive()) {
            this.roleService.enablePermEntryRecursively(req.getRoleName(), req.getDescriptor());
        } else {
            this.roleService.enablePermEntry(req.getRoleName(), req.getDescriptor());
        }
    } //}

    @DeleteMapping()
    private void disablePerm(@RequestParam("descriptor") String descriptor,
            @RequestParam("roleName") String roleName,
            @RequestParam(value = "recursive", defaultValue = "false") boolean recursive) //{
    {
        if(recursive) {
            this.roleService.disablePermEntryRecursively(roleName, descriptor);
        } else {
            this.roleService.disablePermEntry(roleName, descriptor);
        }
    } //}

    @GetMapping()
    private void getPerm(@RequestParam("descriptor") String descriptor,
                         @RequestParam("roleName") String roleName) //{
    {
        if(!this.roleService.hasPermission(roleName, descriptor)) {
            throw new HttpNotFound("角色无权限");
        }
    } //}

    @GetMapping("/list")
    public Collection<RoleService.PermEntryItem> getPermEntryRole(@RequestParam("roleName") String roleName) //{
    {
        return this.roleService.getPermEntries(roleName);
    } //}
}

