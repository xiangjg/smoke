package com.jone.smoke.shiro;

import com.jone.smoke.util.Md5PasswordEncoder;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

public class CredentialsMatcher extends SimpleCredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        UsernamePasswordToken utoken=(UsernamePasswordToken) token;
        //获得数据库中的密码
        String dbPassword=(String) info.getCredentials();
        try {
            //获得用户输入的密码:(可以采用加盐(salt)的方式去检验)
            String inPassword = Md5PasswordEncoder.encrypt(new String(utoken.getPassword()),utoken.getUsername());
            return this.equals(inPassword, dbPassword);
        }catch (Exception e){
            e.printStackTrace();
            return  false;
        }
        //进行密码的比对
    }
}
