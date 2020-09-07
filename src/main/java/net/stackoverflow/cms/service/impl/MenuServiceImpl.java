package net.stackoverflow.cms.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.QueryWrapper;
import net.stackoverflow.cms.dao.MenuDAO;
import net.stackoverflow.cms.model.dto.MenuDTO;
import net.stackoverflow.cms.model.dto.RoleDTO;
import net.stackoverflow.cms.model.entity.Menu;
import net.stackoverflow.cms.model.entity.RoleMenuRef;
import net.stackoverflow.cms.service.MenuService;
import net.stackoverflow.cms.service.RoleMenuRefService;
import net.stackoverflow.cms.service.RoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 菜单服务实现类
 *
 * @author 凉衫薄
 */
@Service
@Slf4j
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDAO menuDAO;

    @Autowired
    private RoleMenuRefService roleMenuRefService;

    @Autowired
    private RoleService roleService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer count() {
        return menuDAO.queryCount(QueryWrapper.newBuilder().build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> findIdsByKeys(List<String> keys) {
        List<String> ids = new ArrayList<>();
        if (!CollectionUtils.isEmpty(keys)) {
            List<Menu> menus = menuDAO.querySelect(QueryWrapper.newBuilder().in("key", keys).build());
            menus.forEach(menu -> ids.add(menu.getId()));
        }
        return ids;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> findKeysByRoleId(String id) {
        List<String> keys = new ArrayList<>();
        List<RoleMenuRef> refs = roleMenuRefService.findByRoleId(id);
        if (!CollectionUtils.isEmpty(refs)) {
            List<String> ids = new ArrayList<>();
            refs.forEach(ref -> ids.add(ref.getMenuId()));
            List<Menu> menus = menuDAO.querySelect(QueryWrapper.newBuilder().in("id", ids).build());
            menus.forEach(menu -> keys.add(menu.getKey()));
        }
        return keys;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<MenuDTO> findTree() {
        List<Menu> menus = menuDAO.querySelect(QueryWrapper.newBuilder().asc("ts").build());
        return toTree(menus);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> findKeysByUserId(String userId) {
        List<RoleDTO> dtos = roleService.findByUserId(userId);
        Set<String> keys = new HashSet<>();
        for (RoleDTO dto : dtos) {
            List<String> ks = findKeysByRoleId(dto.getId());
            keys.addAll(ks);
        }
        return new ArrayList<>(keys);
    }

    private List<MenuDTO> toTree(List<Menu> menus) {
        List<MenuDTO> dtos = new ArrayList<>();
        Map<String, List<MenuDTO>> map = new HashMap<>(16);
        for (Menu menu : menus) {
            if (menu.getParent() != null) {
                MenuDTO dto = new MenuDTO();
                BeanUtils.copyProperties(menu, dto);
                List<MenuDTO> list = map.get(menu.getParent());
                if (list == null) {
                    list = new ArrayList<>();
                }
                list.add(dto);
                map.put(menu.getParent(), list);
            }
        }
        for (Menu menu : menus) {
            if (menu.getParent() == null) {
                MenuDTO dto = new MenuDTO();
                BeanUtils.copyProperties(menu, dto);
                dto.setChildren(map.get(menu.getId()));
                dtos.add(dto);
            }
        }
        return dtos;
    }
}
