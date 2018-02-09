package com.jone.smoke.api.system;


import com.jone.smoke.entity.system.User;

import java.util.List;

public interface UserService {

    void update(User user)throws Exception;
    void save(User user)throws Exception;
    void delete(String userId)throws Exception;
    List<User> queryUsers()throws Exception;
    User queryUserByUserId(String userId)throws Exception;
    void deleteUsers(String[] userIds)throws Exception;
    void updatePassword(String userId, String password)throws Exception;
    void resetPassword(String userId)throws Exception;
    User findUserByIdAndPwd(String userId, String password)throws Exception;
}
