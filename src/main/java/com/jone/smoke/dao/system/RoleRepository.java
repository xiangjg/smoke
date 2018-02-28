package com.jone.smoke.dao.system;

import com.jone.smoke.entity.system.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;


public interface RoleRepository extends JpaRepository<Role,Integer> {
    Role findByRoleId(Integer roleId);
    @Query("update Role r set r.rights = :rights where r.id = :id ")
    @Transactional
    @Modifying
    Integer updateRights(@Param("id") Integer roleId, @Param("rights") BigInteger rights);
}
