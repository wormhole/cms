package net.stackoverflow.cms.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.PageResponse;
import net.stackoverflow.cms.common.QueryWrapper;
import net.stackoverflow.cms.common.QueryWrapper.QueryWrapperBuilder;
import net.stackoverflow.cms.dao.RoleDAO;
import net.stackoverflow.cms.exception.BusinessException;
import net.stackoverflow.cms.model.dto.RoleDTO;
import net.stackoverflow.cms.model.entity.Role;
import net.stackoverflow.cms.model.entity.RoleMenuRef;
import net.stackoverflow.cms.model.entity.UserRoleRef;
import net.stackoverflow.cms.service.MenuService;
import net.stackoverflow.cms.service.RoleMenuRefService;
import net.stackoverflow.cms.service.RoleService;
import net.stackoverflow.cms.service.UserRoleRefService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 角色服务实现类
 *
 * @author 凉衫薄
 */
@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDAO roleDAO;
    @Autowired
    private UserRoleRefService userRoleRefService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleMenuRefService roleMenuRefService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RoleDTO> findAll() {
        List<Role> roles = roleDAO.querySelect(new QueryWrapper());
        List<RoleDTO> roleDTOS = new ArrayList<>();
        for (Role role : roles) {
            RoleDTO roleDTO = new RoleDTO();
            BeanUtils.copyProperties(role, roleDTO);
            roleDTOS.add(roleDTO);
        }
        return roleDTOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageResponse<RoleDTO> findByPage(Integer page, Integer limit, String sort, String order, String key) {
        Set<String> roleIds = new HashSet<>();

        QueryWrapperBuilder builder = new QueryWrapperBuilder();
        builder.sort("builtin", "desc");
        if (StringUtils.isEmpty(sort) || StringUtils.isEmpty(order)) {
            builder.sort("name", "asc");
        } else {
            builder.sort(sort, order);
        }
        builder.like(!StringUtils.isEmpty(key), key, Arrays.asList("name", "note"));
        builder.page((page - 1) * limit, limit);
        builder.in("id", new ArrayList<>(roleIds));
        QueryWrapper wrapper = builder.build();

        List<Role> roles = roleDAO.querySelect(wrapper);
        Integer total = roleDAO.queryCount(wrapper);

        List<RoleDTO> roleDTOS = new ArrayList<>();
        for (Role role : roles) {
            RoleDTO roleDTO = new RoleDTO();
            BeanUtils.copyProperties(role, roleDTO);
            roleDTOS.add(roleDTO);
        }
        return new PageResponse<>(total, roleDTOS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(List<String> ids) {
        if (!CollectionUtils.isEmpty(ids)) {
            List<Role> roles = roleDAO.querySelect(QueryWrapper.newBuilder().in("id", ids).build());
            for (Role role : roles) {
                if (role.getBuiltin().equals(1)) {
                    throw new BusinessException("内建角色不允许被删除");
                }
            }
            roleDAO.batchDelete(ids);
            roleMenuRefService.deleteByRoleIds(ids);
            userRoleRefService.deleteByRoleIds(ids);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RoleDTO roleDTO) {
        QueryWrapperBuilder builder = new QueryWrapperBuilder();
        builder.neq("id", roleDTO.getId());
        builder.eq("name", roleDTO.getName());
        List<Role> roles = roleDAO.querySelect(builder.build());
        if (!CollectionUtils.isEmpty(roles)) {
            throw new BusinessException("角色名不能重复");
        }

        QueryWrapperBuilder deleteBuilder = new QueryWrapperBuilder();
        deleteBuilder.update("name", roleDTO.getName());
        deleteBuilder.update("note", roleDTO.getNote());
        deleteBuilder.update("ts", new Date());
        deleteBuilder.eq("id", roleDTO.getId());
        roleDAO.queryUpdate(deleteBuilder.build());

        roleMenuRefService.deleteByRoleId(roleDTO.getId());
        List<String> ids = menuService.findIdsByKeys(roleDTO.getMenus());
        List<RoleMenuRef> refs = new ArrayList<>();
        for (String id : ids) {
            RoleMenuRef ref = new RoleMenuRef();
            ref.setId(UUID.randomUUID().toString());
            ref.setMenuId(id);
            ref.setRoleId(roleDTO.getId());
            ref.setTs(new Date());
            refs.add(ref);
        }
        if (!CollectionUtils.isEmpty(refs)) {
            roleMenuRefService.batchSave(refs);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(RoleDTO roleDTO) {
        QueryWrapperBuilder builder = new QueryWrapperBuilder();
        builder.eq("name", roleDTO.getName());
        List<Role> roles = roleDAO.querySelect(builder.build());
        if (!CollectionUtils.isEmpty(roles)) {
            throw new BusinessException("角色名不能重复");
        }

        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        role.setId(UUID.randomUUID().toString());
        role.setBuiltin(0);
        role.setTs(new Date());
        roleDAO.insert(role);

        List<String> ids = menuService.findIdsByKeys(roleDTO.getMenus());
        List<RoleMenuRef> refs = new ArrayList<>();
        for (String id : ids) {
            RoleMenuRef ref = new RoleMenuRef();
            ref.setId(UUID.randomUUID().toString());
            ref.setMenuId(id);
            ref.setRoleId(role.getId());
            ref.setTs(new Date());
            refs.add(ref);
        }
        if (!CollectionUtils.isEmpty(refs)) {
            roleMenuRefService.batchSave(refs);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer count() {
        return roleDAO.queryCount(new QueryWrapper());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RoleDTO> findByUserId(String userId) {
        List<UserRoleRef> userRoleRefs = userRoleRefService.findByUserId(userId);
        List<RoleDTO> roleDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userRoleRefs)) {
            List<String> roleIds = new ArrayList<>();
            userRoleRefs.forEach(userRoleRef -> roleIds.add(userRoleRef.getRoleId()));
            List<Role> roles = roleDAO.querySelect(QueryWrapper.newBuilder().in("id", roleIds).build());
            for (Role role : roles) {
                RoleDTO dto = new RoleDTO();
                BeanUtils.copyProperties(role, dto);
                dto.setMenus(menuService.findKeysByRoleId(role.getId()));
                roleDTOS.add(dto);
            }
        }
        return roleDTOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<String> findNamesByUserId(String userId) {
        List<UserRoleRef> userRoleRefs = userRoleRefService.findByUserId(userId);
        List<String> names = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userRoleRefs)) {
            List<String> roleIds = new ArrayList<>();
            userRoleRefs.forEach(userRoleRef -> roleIds.add(userRoleRef.getRoleId()));
            List<Role> roles = roleDAO.querySelect(QueryWrapper.newBuilder().in("id", roleIds).build());
            roles.forEach(role -> names.add(role.getName()));
        }
        return names;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoleDTO findById(String id) {
        Role role = roleDAO.select(id);
        if (role != null) {
            RoleDTO dto = new RoleDTO();
            BeanUtils.copyProperties(role, dto);
            dto.setMenus(menuService.findKeysByRoleId(role.getId()));
            return dto;
        } else {
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Role findByName(String name) {
        QueryWrapperBuilder builder = new QueryWrapperBuilder();
        builder.eq("name", name);
        List<Role> roles = roleDAO.querySelect(builder.build());
        if (!CollectionUtils.isEmpty(roles)) {
            return roles.get(0);
        } else {
            return null;
        }
    }

}
