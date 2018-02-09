package com.jone.smoke.service.system;

import com.jone.smoke.api.system.UserService;
import com.jone.smoke.dao.system.UserRepository;
import com.jone.smoke.entity.system.User;
import com.jone.smoke.util.Md5PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public void update(User user)throws Exception {
        userRepository.updateUserByUserId(user.getUserId(),user.getUserName(),user.getSex(),user.getMobile(),user.getRole().getRoleId());
    }

    @Override
    public void save(User user)throws Exception {
        user.setPassword(Md5PasswordEncoder.encrypt(user.getPassword(),user.getUserId()));
        userRepository.saveAndFlush(user);
    }

    @Override
    public void delete(String userId)throws Exception {
        userRepository.delete(userRepository.findByUserId(userId));
    }

    @Override
    public List<User> queryUsers() throws Exception {
        return userRepository.findAll();
    }

    @Override
    public User queryUserByUserId(String userId) throws Exception {
        return userRepository.findByUserId(userId);
    }

    @Override
    public void deleteUsers(String[] userIds) throws Exception {
        for (String userId: userIds
             ) {
            userRepository.delete(userRepository.findByUserId(userId));
        }
    }

    @Override
    public void updatePassword(String userId, String password) throws Exception {
        userRepository.modifyPasswordByUserId(Md5PasswordEncoder.encrypt(password,userId),userId);
    }

    @Override
    public void resetPassword(String userId) throws Exception {
        userRepository.modifyPasswordByUserId(Md5PasswordEncoder.encrypt("123456",userId),userId);
    }

    @Override
    public User findUserByIdAndPwd(String userId, String password) throws Exception {
        return userRepository.findByUserIdAndPassword(userId, Md5PasswordEncoder.encrypt(password,userId));
    }
}
