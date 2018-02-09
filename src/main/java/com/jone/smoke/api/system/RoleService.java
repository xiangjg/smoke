package com.jone.smoke.api.system;



import com.jone.smoke.entity.system.MenuInfo;
import com.jone.smoke.entity.system.Role;

import java.util.List;

public interface RoleService {

    void save(Role role);
    void deleteById(String roleid);
    void deleteByRoles(String[] roleids);
    void update(Role role);
    void updateRights(Integer roleid, List<MenuInfo> rights);
    Role getRoleByUser(String userid);
    Role getRoleById(Integer roleid);
    List<Role> getAllRole();
    Role getRoleMenuInfo(Role role);
    Role getRoleMenuInfo(String roleid);
}
