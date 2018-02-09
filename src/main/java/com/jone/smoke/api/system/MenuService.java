package com.jone.smoke.api.system;


import com.jone.smoke.entity.system.MenuInfo;

import java.util.List;

public interface MenuService {

    List<MenuInfo> queryMenuInfo()throws Exception;
}
