package com.jone.smoke.entity.system;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="sys_user",schema="sysdb")
public class User implements Serializable {

    private Integer id;
    private String userId;
    private String userName;
    private String password;
    private Integer sex;
    private String mobile;
    private Role role;
    public void User(){
    	
    }
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name="password",length = 50,nullable = false)
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Column(name="sex",length = 1)
	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}
	@Column(name="mobile",length = 20)
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	@Column(name="user_id",nullable = false,length = 18)
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	@Column(name="user_name",length = 10,nullable = false)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	@ManyToOne(targetEntity = Role.class,fetch = FetchType.EAGER)
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
