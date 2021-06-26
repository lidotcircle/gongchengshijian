package six.daoyun.controller.admin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import six.daoyun.controller.exception.HttpBadRequest;
import six.daoyun.controller.exception.HttpNotFound;
import six.daoyun.entity.Role;
import six.daoyun.entity.User;
import six.daoyun.exchange.UserInfo;
import six.daoyun.service.RoleService;
import six.daoyun.service.UserService;
import six.daoyun.utils.ObjUtil;


@Tag(name = "用户管理(管理员)")
@RequestMapping("/apis/admin/user")
@RestController
public class AdminUser {
	@Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    static class UserDTO extends UserInfo //{
    {
        @NotNull
        private String userName;
        public String getUserName() {
            return this.userName;
        }
        public void setUserName(String userName) {
            this.userName = userName;
        }

        @NotNull
        private String password;
        public String getPassword() {
            return this.password;
        }
        public void setPassword(String password) {
            this.password = password;
        }

        private String role;
        public String getRole() {
            return this.role;
        }
        public void setRole(String role) {
            this.role = role;
        }
    } //}
    static class PageRespDTO //{
    {
        private long total;
        public long getTotal() {
            return this.total;
        }
        public void setTotal(long total) {
            this.total = total;
        }

        private Collection<UserDTO> pairs;
        public Collection<UserDTO> getPairs() {
            return this.pairs;
        }
        public void setPairs(Collection<UserDTO> pairs) {
            this.pairs = pairs;
        }
    } //}

    @PostMapping()
    private void createUser(@RequestBody @Valid UserDTO req) //{
    {
        final User user = new User();
        this.UserDtoToUser(req, user);
        user.setThirdPartyAccountType("none");
        this.userService.createUser(user);
    } //}

    @PutMapping()
    private void updateUser(@RequestBody UserDTO req) //{
    {
        if(req.getUserName() == null) {
            throw new HttpBadRequest("require userName field");
        }
        final User user = this.userService.getUser(req.getUserName())
            .orElseThrow(() -> new HttpNotFound("用户 '" + req.getUserName() + "' 不存在"));
        this.UserDtoToUser(req, user);
        this.userService.updateUser(user);
    } //}

    @GetMapping()
    private UserDTO getUser(@RequestParam("userName") String username) //{
    {
        final User user = this.userService.getUser(username)
            .orElseThrow(() -> new HttpNotFound("用户 '" + username + "' 不存在"));
        return this.userTodUserDto(user);
    } //}

    @DeleteMapping()
    private void deleteUser(@RequestParam("userName") String username) //{
    {
        this.userService.deleteUser(username);
    } //}

    private UserDTO userTodUserDto(User user) //{
    {
        UserDTO ans = new UserDTO();
        ObjUtil.assignFields(ans, user);

        if(user.getBirthday() != null) {
            ans.setBirthday(user.getBirthday().getTime());
        }

        if(user.getProfilePhoto() != null && user.getProfilePhoto().length > 0) {
            ans.setPhoto(new String(user.getProfilePhoto()));
        }

        Collection<String> roles = new ArrayList<>();
        user.getRoles().forEach(role -> roles.add(role.getRoleName()));
        ans.setRoles(roles);

        ans.setPassword(null);

        String rolestr = "";
        for(var role: user.getRoles()) {
            rolestr += role.getRoleName() + ", ";
        }
        if(rolestr.endsWith(", ")) {
            rolestr = rolestr.substring(0, rolestr.length() - 2);
        } else {
            rolestr = "无";
        }
        ans.setRole(rolestr);
        return ans;
    } //}
    private void UserDtoToUser(UserDTO dto, User user) //{
    {
        final Set<String> ignores = new HashSet<>();
        ignores.add("password");
        ObjUtil.assignFields(user, dto, ignores, true);

        if(dto.getBirthday() >= 0) {
            Date birthday = new Date(dto.getBirthday());
            user.setBirthday(birthday);
        }

        if(dto.getPhoto() != null && dto.getPhoto().length() > 0) {
            byte[] photo = dto.getPhoto().getBytes();
            user.setProfilePhoto(photo);
        }

        if(dto.getPassword() != null) {
            user.setPassword(this.passwordEncoder.encode(dto.getPassword()));
        }

        if(dto.getRoles() != null) {
            Collection<Role> roles = new ArrayList<>();
            for (var rolestr: dto.getRoles()) {
                var role = this.roleService.getRoleByRoleName(rolestr)
                    .orElseThrow(() -> new HttpNotFound(String.format("角色名%s不存在", rolestr)));
                log.info("asdfzxcv {}", rolestr);
                roles.add(role);
            }
            user.setRoles(roles);
        }
    } //}
private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(AdminUser.class);

    @GetMapping("/page")
    private PageRespDTO getUserPage(@RequestParam("pageno") int pageno,
                                               @RequestParam("size") int size,
                                               @RequestParam(value = "sortKey", defaultValue = "userName") String sortKey,
                                               @RequestParam(value = "sortDir", defaultValue = "DESC") String sortDir,
                                               @RequestParam(value = "searchWildcard", required = false) String wildcard) //{
    {
        final PageRespDTO ans = new PageRespDTO();

        Page<User> page = 
            this.userService.getUserPage(pageno - 1, size, sortKey, 
                                         "desc".equalsIgnoreCase(sortDir), wildcard);

        final Collection<UserDTO> pairs = new ArrayList<>();
        page.forEach(v -> pairs.add(this.userTodUserDto(v)));
        ans.setPairs(pairs);
        ans.setTotal(page.getTotalElements());

        return ans;
    } //}
}

