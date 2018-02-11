package com.jone.smoke.shiro;

import com.jone.smoke.api.system.MenuService;
import com.jone.smoke.api.system.RoleService;
import com.jone.smoke.api.system.UserService;
import com.jone.smoke.entity.system.MenuInfo;
import com.jone.smoke.entity.system.Role;
import com.jone.smoke.entity.system.User;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class AuthRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;

    //认证.登录
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken utoken = (UsernamePasswordToken) token;//获取用户输入的token
        String username = utoken.getUsername();
        User user = null;
        try {
            user = userService.queryUserByUserId(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SimpleAuthenticationInfo(user, user.getPassword(), this.getClass().getName());//放入shiro.调用CredentialsMatcher检验密码
    }

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        User user = (User) principal.fromRealm(this.getClass().getName()).iterator().next();//获取session中的用户
        List<String> permissions = new ArrayList<>();
        Role role = user.getRole();
        List<MenuInfo> list = null;
        try {
            list = menuService.queryMenuInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<MenuInfo> noList = new ArrayList<>();
        for (MenuInfo mi : list
                ) {
            if (!role.getRights().testBit(mi.getMenuId()))
                noList.add(mi);
        }
        for (MenuInfo mi : noList
                ) {
            list.remove(mi);
        }
        for (MenuInfo mi : list
                ) {
            permissions.add(mi.getMenuName());
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addStringPermissions(permissions);//将权限放入shiro中.
        return info;
    }
}
