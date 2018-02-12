package com.jone.smoke.entity.system;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="sys_menu",schema="sysdb")
public class MenuInfo implements Serializable{
	@Id
	@GeneratedValue
	@Column(name = "menuid", unique = true, nullable = false)
	private Integer menuId;
	@Column(name = "menuname", nullable = false,length = 12)
	private String menuName;
	@Column(name = "menuurl",length = 20)
	private String menuUrl;
	@Column(name = "parentid", nullable = false)
	private Integer parentId;
	@Column(name = "menuicon",length = 50)
	private String menuIcon;
	@Column(name = "number")
	private Integer number;
	@Transient//非数据库字段
	private Integer depth;
	@Transient//非数据库字段
	private Boolean have;
	@Transient//非数据库字段
	private List<MenuInfo> child;

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getMenuUrl() {
		return menuUrl;
	}

	public void setMenuUrl(String menuUrl) {
		this.menuUrl = menuUrl;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getMenuIcon() {
		return menuIcon;
	}

	public void setMenuIcon(String menuIcon) {
		this.menuIcon = menuIcon;
	}

	public Integer getDepth() {
		return depth;
	}
	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	public Boolean getHave() {
		return have;
	}

	public void setHave(Boolean have) {
		this.have = have;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public List<MenuInfo> getChild() {
		return child;
	}

	public void setChild(List<MenuInfo> child) {
		this.child = child;
	}
}
