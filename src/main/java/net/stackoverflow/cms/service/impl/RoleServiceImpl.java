package net.stackoverflow.cms.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.PageResponse;
import net.stackoverflow.cms.common.QueryWrapper;
import net.stackoverflow.cms.common.QueryWrapper.QueryWrapperBuilder;
import net.stackoverflow.cms.dao.PermissionDAO;
import net.stackoverflow.cms.dao.RoleDAO;
import net.stackoverflow.cms.dao.RolePermissionRefDAO;
import net.stackoverflow.cms.exception.BusinessException;
import net.stackoverflow.cms.model.dto.PermissionDTO;
import net.stackoverflow.cms.model.dto.RoleDTO;
import net.stackoverflow.cms.model.entity.Permission;
import net.stackoverflow.cms.model.entity.Role;
import net.stackoverflow.cms.model.entity.RolePermissionRef;
import net.stackoverflow.cms.service.RoleService;
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
    private RolePermissionRefDAO rolePermissionRefDAO;
    @Autowired
    private PermissionDAO permissionDAO;

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
        if (!CollectionUtils.isEmpty(roleIds)) {
            QueryWrapperBuilder builder = new QueryWrapperBuilder();
            builder.in("permissionId", permissionIds.toArray());
            List<RolePermissionRef> rolePermissionRefs = rolePermissionRefDAO.selectByCondition(builder.build());
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
        builder.in("id", roleIds.toArray());
        QueryWrapper wrapper = builder.build();

        List<Role> roles = roleDAO.selectByCondition(wrapper);
        Integer total = roleDAO.countByCondition(wrapper);

        List<RoleDTO> roleDTOS = new ArrayList<>();
        for (Role role : roles) {
            RoleDTO roleDTO = new RoleDTO();
            BeanUtils.copyProperties(role, roleDTO);
            List<PermissionDTO> permissionDTOS = findPermissionByRoleId(role.getId());
            roleDTO.setPermissions(permissionDTOS);
            roleDTOS.add(roleDTO);
        }
        return new PageResponse<>(total, roleDTOS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<PermissionDTO> findPermissionByRoleId(String roleId) {
        QueryWrapperBuilder builder = new QueryWrapperBuilder();
        builder.eq("roleId", roleId);
        List<RolePermissionRef> rolePermissionRefs = rolePermissionRefDAO.selectByCondition(builder.build());
        List<PermissionDTO> permissionDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(rolePermissionRefs)) {
            List<String> permissionIds = new ArrayList<>();
            rolePermissionRefs.forEach(rolePermissionRef -> permissionIds.add(rolePermissionRef.getPermissionId()));
            List<Permission> permissions = permissionDAO.selectByCondition(QueryWrapper.newBuilder().in("id", permissionIds.toArray()).build());
            permissions.forEach(permission -> {
                PermissionDTO permissionDTO = new PermissionDTO();
                BeanUtils.copyProperties(permission, permissionDTO);
                permissionDTOS.add(permissionDTO);
            });
        }
        return permissionDTOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(List<String> ids) {
        List<Role> roles = roleDAO.selectByCondition(new QueryWrapper());
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
        rolePermissionRefDAO.deleteByCondition(QueryWrapper.newBuilder().eq("roleId", roleId).build());
        List<RolePermissionRef> rolePermissionRefs = new ArrayList<>();
        permissionIds.forEach(permissionId -> {
            rolePermissionRefs.add(new RolePermissionRef(UUID.randomUUID().toString(), roleId, permissionId, new Date()));
        });
        rolePermissionRefDAO.batchInsert(rolePermissionRefs);
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
        Role role = roleDAO.select(roleDTO.getId());
        if (role.getBuiltin().equals(1)) {
            throw new BusinessException("内建角色不允许被修改");
        }

        BeanUtils.copyProperties(roleDTO, role);
        role.setTs(new Date());
        roleDAO.update(role);
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

}
