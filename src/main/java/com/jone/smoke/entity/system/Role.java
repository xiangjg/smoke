package com.jone.smoke.entity.system;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

@Entity
@Table(name="sys_role",schema="sysdb")
public class Role implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id", unique = true, nullable = false)
    private Integer roleId;
    @Column(name="role_name", nullable = false,length = 20)
    private String roleName;
    @Column(name="rights")
    private BigInteger rights;
    @Column(name="role_pid")
    private Integer rolePid;
    @Transient//非数据库字段
    private List<MenuInfo> mis;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public BigInteger getRights() {
        return rights;
    }

    public void setRights(BigInteger rights) {
        this.rights = rights;
    }

    public Integer getRolePid() {
        return rolePid;
    }

    public void setRolePid(Integer rolePid) {
        this.rolePid = rolePid;
    }

    public List<MenuInfo> getMis() {
        return mis;
    }

    public void setMis(List<MenuInfo> mis) {
        this.mis = mis;
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleId=" + roleId +
                ", roleName='" + roleName + '\'' +
                ", rights=" + rights +
                ", rolePid=" + rolePid +
                '}';
    }
}
