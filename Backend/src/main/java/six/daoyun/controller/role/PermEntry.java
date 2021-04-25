package six.daoyun.controller.role;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import six.daoyun.service.RoleService;
import six.daoyun.utils.ObjUitl;


@RestController
@RequestMapping("/apis/perm")
public class PermEntry {
    @Autowired
    private RoleService roleService;

    @GetMapping
    public RoleService.PermEntryItem getPermEntry(@RequestParam("descriptor") String descriptor) //{
    {
        return this.roleService.getPermEntryItemByDescriptor(descriptor);
    } //}

    @DeleteMapping
    public void deletePermEntry(@RequestParam("descriptor") String descriptor,
            @RequestParam(name = "recursive", defaultValue = "false") boolean recursive) //{
    {
        if(recursive) {
            this.roleService.removePermEntryRecursively(descriptor);
        } else {
            this.roleService.removePermEntry(descriptor);
        }
    } //}

    static class PermEntryCreationDTO //{
    {
        @NotNull
        @Pattern(regexp = "([a-zA-Z][a-zA-Z0-9_]{1,}\\.)*[a-zA-Z][a-zA-Z0-9_]{1,}")
        private String descriptor;
        public String getDescriptor() {
            return this.descriptor;
        }
        public void setDescriptor(String descriptor) {
            this.descriptor = descriptor;
        }

        @Pattern(regexp = "([a-zA-Z][a-zA-Z0-9_]{1,}\\.)*[a-zA-Z][a-zA-Z0-9_]{1,}")
        private String parentDescriptor;
        public String getParentDescriptor() {
            return this.parentDescriptor;
        }
        public void setParentDescriptor(String parentDescriptor) {
            this.parentDescriptor = parentDescriptor;
        }

        @NotNull
        private String link;
        public String getLink() {
            return this.link;
        }
        public void setLink(String link) {
            this.link = link;
        }

        @NotNull
        private String entryType;
        public String getEntryType() {
            return this.entryType;
        }
        public void setEntryType(String entryType) {
            this.entryType = entryType;
        }

        @NotNull
        private String permEntryName;
        public String getPermEntryName() {
            return this.permEntryName;
        }
        public void setPermEntryName(String permEntryName) {
            this.permEntryName = permEntryName;
        }
    } //}
    @PostMapping
    public void createPermEntry(@RequestBody @Valid PermEntryCreationDTO entry) //{
    {
        RoleService.PermEntryItem pentry = new RoleService.PermEntryItem();
        ObjUitl.assignFields(pentry, entry);
        this.roleService.addPermEntry(entry.getParentDescriptor(), pentry);
    } //}

    static class PermEntryUpdateDTO //{
    {
        @NotNull
        private String descriptor;
        public String getDescriptor() {
            return this.descriptor;
        }
        public void setDescriptor(String descriptor) {
            this.descriptor = descriptor;
        }

        private String link;
        public String getLink() {
            return this.link;
        }
        public void setLink(String link) {
            this.link = link;
        }

        private String entryType;
        public String getEntryType() {
            return this.entryType;
        }
        public void setEntryType(String entryType) {
            this.entryType = entryType;
        }

        private String permEntryName;
        public String getPermEntryName() {
            return this.permEntryName;
        }
        public void setPermEntryName(String permEntryName) {
            this.permEntryName = permEntryName;
        }
    } //}
    @PutMapping
    public void updatePermEntry(@RequestBody PermEntryUpdateDTO entry) //{
    {
        RoleService.PermEntryItem item = new RoleService.PermEntryItem();
        ObjUitl.assignFields(item, entry);
        this.roleService.updatePermEntry(entry.descriptor, item);
    } //}

    @GetMapping("/tree")
    public Collection<RoleService.PermEntryTree> getPermEntryTree() //{
    {
        return this.roleService.getPermEntryTree();
    } //}

    @GetMapping("/role")
    public Collection<String> getPermEntryRoles(@RequestParam("descriptor") String descriptor) //{
    {
        Collection<String> ans = new ArrayList<>();
        this.roleService.getRolesByPermEntry(descriptor).forEach(role -> ans.add(role.getRoleName()));
        return ans;
    } //}
}

