package com.jone.smoke.dao.system;

import com.jone.smoke.entity.system.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface UserRepository extends JpaRepository<User,Integer> {

    User findByUserName(String userName);
    User findByUserId(String userId);
    @Transactional
    @Modifying
    @Query("update User u set u.userName = ?1 where u.userId = ?2")
    int modifyUserNameByUserId(String userName, String userId);
    @Transactional
    @Modifying
    @Query("update User u set u.password = ?1 where u.userId = ?2")
    int modifyPasswordByUserId(String password, String userId);

    User findByUserIdAndPassword(String userId, String password);
    @Transactional
    @Modifying
    @Query("update User u set u.userName = :userName,u.sex = :sex,u.mobile = :mobile,u.role.roleId = :roleId where u.userId = :userId")
    int updateUserByUserId(@Param("userId") String userId, @Param("userName") String userName, @Param("sex") Integer sex, @Param("mobile") String mobile, @Param("roleId") Integer roleId);
}
