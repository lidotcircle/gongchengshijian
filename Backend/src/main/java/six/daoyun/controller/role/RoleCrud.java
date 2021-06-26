package six.daoyun.controller.role;

import java.util.Collection;
import java.util.Date;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import six.daoyun.controller.exception.HttpForbidden;
import six.daoyun.controller.exception.HttpNotFound;
import six.daoyun.entity.Role;
import six.daoyun.service.RoleService;


@Tag(name = "角色管理")
@RestController
@RequestMapping("/apis/role")
class RoleCrud {
    @Autowired
    private RoleService roleService;

    static class RoleDTO //{
    {
        @NotNull
        @Pattern(regexp = "\\w{2,}")
        private String roleName;
        public String getRoleName() {
            return this.roleName;
        }
        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        private Date createdDate;
        public Date getCreatedDate() {
            return this.createdDate;
        }
        public void setCreatedDate(Date createdDate) {
            this.createdDate = createdDate;
        }

        private Date modifiedDate;
        public Date getModifiedDate() {
            return this.modifiedDate;
        }
        public void setModifiedDate(Date modifiedDate) {
            this.modifiedDate = modifiedDate;
        }
    } //}

    @GetMapping("/list")
    private Collection<Role> getRoleList() {
        return this.roleService.getAllRoles();
    }

    @PostMapping()
    private void createRole(@RequestBody @Valid RoleDTO role) {
        if(this.roleService.hasRole(role.getRoleName())) {
            throw new HttpForbidden("角色已存在");
        } else {
            this.roleService.createRole(role.getRoleName());
        }
    }

    @DeleteMapping()
    private void deleteRole(@RequestParam String roleName) {
        if(!this.roleService.hasRole(roleName)) {
            throw new HttpNotFound("角色不存在");
        } else {
            this.roleService.deleteRole(roleName);
        }
    }
}

