package net.stackoverflow.cms.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.PageResponse;
import net.stackoverflow.cms.common.QueryWrapper;
import net.stackoverflow.cms.common.QueryWrapper.QueryWrapperBuilder;
import net.stackoverflow.cms.dao.RoleDAO;
import net.stackoverflow.cms.exception.BusinessException;
import net.stackoverflow.cms.model.dto.PermissionDTO;
import net.stackoverflow.cms.model.dto.RoleDTO;
import net.stackoverflow.cms.model.entity.Role;
import net.stackoverflow.cms.model.entity.RolePermissionRef;
import net.stackoverflow.cms.model.entity.UserRoleRef;
import net.stackoverflow.cms.service.PermissionService;
import net.stackoverflow.cms.service.RolePermissionRefService;
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
    private RolePermissionRefService rolePermissionRefService;
    @Autowired
    private PermissionService permissionService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RoleDTO> findAll() {
        List<Role> roles = roleDAO.selectByCondition(new QueryWrapper());
        List<RoleDTO> roleDTOS = new ArrayList<>();
        roles.forEach(role -> {
            RoleDTO roleDTO = new RoleDTO();
            BeanUtils.copyProperties(role, roleDTO);
            roleDTOS.add(roleDTO);
        });
        return roleDTOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageResponse<RoleDTO> findByPage(Integer page, Integer limit, String sort, String order, String key, List<String> permissionIds) {
        Set<String> roleIds = new HashSet<>();
        if (!CollectionUtils.isEmpty(permissionIds)) {
            List<RolePermissionRef> rolePermissionRefs = rolePermissionRefService.findByPermissionIds(permissionIds);
            if (!CollectionUtils.isEmpty(rolePermissionRefs)) {
                rolePermissionRefs.forEach(rolePermissionRef -> roleIds.add(rolePermissionRef.getRoleId()));
            } else {
                return new PageResponse<>(0, new ArrayList<>());
            }
        }

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

        List<Role> roles = roleDAO.selectByCondition(wrapper);
        Integer total = roleDAO.countByCondition(wrapper);

        List<RoleDTO> roleDTOS = new ArrayList<>();
        for (Role role : roles) {
            RoleDTO roleDTO = new RoleDTO();
            BeanUtils.copyProperties(role, roleDTO);
            List<PermissionDTO> permissionDTOS = permissionService.findByRoleId(role.getId());
            roleDTO.setPermissions(permissionDTOS);
            roleDTOS.add(roleDTO);
        }
        return new PageResponse<>(total, roleDTOS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(List<String> ids) {
        List<Role> roles = roleDAO.selectByCondition(QueryWrapper.newBuilder().in("id", ids).build());
        for (Role role : roles) {
            if (role.getBuiltin().equals(1)) {
                throw new BusinessException("内建角色不允许被删除");
            }
        }
        roleDAO.batchDelete(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reGrantPermission(String roleId, List<String> permissionIds) {
        rolePermissionRefService.deleteByRoleId(roleId);
        List<RolePermissionRef> rolePermissionRefs = new ArrayList<>();
        permissionIds.forEach(permissionId -> {
            rolePermissionRefs.add(new RolePermissionRef(UUID.randomUUID().toString(), roleId, permissionId, new Date()));
        });
        if (!CollectionUtils.isEmpty(rolePermissionRefs)) {
            rolePermissionRefService.batchSave(rolePermissionRefs);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(RoleDTO roleDTO) {
        QueryWrapperBuilder builder = new QueryWrapperBuilder();
        builder.neq("id", roleDTO.getId());
        builder.eq("name", roleDTO.getName());
        List<Role> roles = roleDAO.selectByCondition(builder.build());
        if (!CollectionUtils.isEmpty(roles)) {
            throw new BusinessException("角色名不能重复");
        }

        roleDAO.updateByCondition(QueryWrapper.newBuilder()
                .update("name", roleDTO.getName())
                .update("note", roleDTO.getNote())
                .update("ts", new Date())
                .eq("id", roleDTO.getId()).build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(RoleDTO roleDTO) {
        QueryWrapperBuilder builder = new QueryWrapperBuilder();
        builder.eq("name", roleDTO.getName());
        List<Role> roles = roleDAO.selectByCondition(builder.build());
        if (!CollectionUtils.isEmpty(roles)) {
            throw new BusinessException("角色名不能重复");
        }

        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        role.setId(UUID.randomUUID().toString());
        role.setBuiltin(0);
        role.setTs(new Date());
        roleDAO.insert(role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer count() {
        return roleDAO.countByCondition(new QueryWrapper());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RoleDTO> findByUserId(String userId) {
        List<UserRoleRef> userRoleRefs = userRoleRefService.findByUserId(userId);
        List<RoleDTO> roleDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(userRoleRefs)) {
            List<String> roleIds = new ArrayList<>();
            userRoleRefs.forEach(userRoleRef -> {
                roleIds.add(userRoleRef.getRoleId());
            });
            List<Role> roles = roleDAO.selectByCondition(QueryWrapper.newBuilder().in("id", roleIds).build());
            roles.forEach(role -> {
                RoleDTO dto = new RoleDTO();
                BeanUtils.copyProperties(role, dto);
                roleDTOS.add(dto);
            });
        }
        return roleDTOS;
    }

}
