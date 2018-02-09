package com.jone.smoke.dao.system;


import com.jone.smoke.entity.system.MenuInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<MenuInfo,Integer> {

    List<MenuInfo> findByParentId(Integer pid);

}
