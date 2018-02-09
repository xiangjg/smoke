package com.jone.smoke.service.system;

import com.jone.smoke.api.system.MenuService;
import com.jone.smoke.dao.system.MenuRepository;
import com.jone.smoke.entity.system.MenuInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service(value = "menuService")
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuRepository menuRepository;
    @Override
    public List<MenuInfo> queryMenuInfo() throws Exception {
        Sort sort = new Sort(Sort.Direction.ASC, "number");
        List<MenuInfo> list = menuRepository.findAll(sort);
        List<MenuInfo> menus = new ArrayList<>();
        for (MenuInfo m:list
             ) {
            m.setHave(true);
            if(m.getParentId() == 0)
                menus.add(m);
        }
        for (MenuInfo menu:menus
             ) {
            for (MenuInfo m:list
                    ) {
                if(menu.getChild() == null)
                    menu.setChild(new ArrayList<>());
                if(m.getParentId() == menu.getMenuId())
                    menu.getChild().add(m);
            }
        }
        return menus;
    }
}
