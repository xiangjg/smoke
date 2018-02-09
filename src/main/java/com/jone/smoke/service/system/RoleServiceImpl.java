package com.jone.smoke.service.system;

import com.jone.smoke.api.system.RoleService;
import com.jone.smoke.dao.IDao;
import com.jone.smoke.dao.system.MenuRepository;
import com.jone.smoke.dao.system.RoleRepository;
import com.jone.smoke.dao.system.UserRepository;
import com.jone.smoke.entity.system.MenuInfo;
import com.jone.smoke.entity.system.Role;
import com.jone.smoke.entity.system.User;
import com.jone.smoke.util.BigIntegerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service(value = "roleService")
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private IDao dao;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public void updateRights(Integer roleid, List<MenuInfo> rights) {
        Role role = roleRepository.findByRoleId(roleid);
        List<Integer> list = new ArrayList<>();
        for (MenuInfo mi:rights
             ) {
            if(mi.getHave())
                list.add(mi.getMenuId());
        }
        role.setRights(BigIntegerUtils.sumRights(list));
        roleRepository.updateRights(role.getRoleId(),role.getRights());
    }

    @Override
    public void save(Role role) {
        roleRepository.save(role);
    }

    @Override
    public void deleteById(String roleid) {
        dao.delete(roleRepository.findByRoleId(Integer.valueOf(roleid)));
    }

    @Override
    public void update(Role role) {
        dao.update(role);
    }

    @Override
    public Role getRoleByUser(String userid) {
        User user = userRepository.findByUserId(userid);
        return user.getRole();
    }

    @Override
    public List<Role> getAllRole() {
        List<Role> roles = roleRepository.findAll();
        for (Role role : roles
                ) {
            role = getRoleMenuInfo(role);
        }
        return roles;
    }

    @Override
    public Role getRoleById(Integer roleid) {
        return roleRepository.findByRoleId(roleid);
    }

    @Override
    public Role getRoleMenuInfo(Role role) {
        List<MenuInfo> mis = menuRepository.findAll();
        role.setMis(mis);
        for (MenuInfo mi:role.getMis()
             ) {
            if (role.getRights()!=null&&role.getRights().testBit(mi.getMenuId()))
                mi.setHave(true);
            else
                mi.setHave(false);
        }
        return role;
    }

    @Override
    public Role getRoleMenuInfo(String roleid) {
        return getRoleMenuInfo(getRoleById(Integer.valueOf(roleid)));
    }

    @Override
    public void deleteByRoles(String[] roleids) {
        for (String roleId: roleids
             ) {
            roleRepository.delete(roleRepository.findByRoleId(Integer.valueOf(roleId)));
        }
    }
}
