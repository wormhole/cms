package net.stackoverflow.cms.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.PageResponse;
import net.stackoverflow.cms.common.QueryWrapper;
import net.stackoverflow.cms.common.QueryWrapper.QueryWrapperBuilder;
import net.stackoverflow.cms.dao.PermissionDAO;
import net.stackoverflow.cms.exception.BusinessException;
import net.stackoverflow.cms.model.dto.PermissionDTO;
import net.stackoverflow.cms.model.entity.Permission;
import net.stackoverflow.cms.service.PermissionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 权限服务实现类
 *
 * @author 凉衫薄
 */
@Service
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private PermissionDAO permissionDAO;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<PermissionDTO> findAll() {
        List<Permission> permissions = permissionDAO.selectByCondition(new QueryWrapper());
        List<PermissionDTO> permissionDTOS = new ArrayList<>();
        permissions.forEach(permission -> {
            PermissionDTO permissionDTO = new PermissionDTO();
            BeanUtils.copyProperties(permission, permissionDTO);
            permissionDTOS.add(permissionDTO);
        });
        return permissionDTOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PageResponse<PermissionDTO> findByPage(Integer page, Integer limit, String sort, String order, String key) {
        QueryWrapperBuilder builder = new QueryWrapperBuilder();
        builder.sort("builtin", "desc");
        if (StringUtils.isEmpty(sort) || StringUtils.isEmpty(order)) {
            builder.sort("name", "asc");
        } else {
            builder.sort(sort, order);
        }
        builder.like(!StringUtils.isEmpty(key), key, Arrays.asList("name", "note"));
        builder.page((page - 1) * limit, limit);
        QueryWrapper wrapper = builder.build();

        List<Permission> permissions = permissionDAO.selectByCondition(wrapper);
        Integer total = permissionDAO.countByCondition(wrapper);

        List<PermissionDTO> permissionDTOS = new ArrayList<>();
        for (Permission permission : permissions) {
            PermissionDTO permissionDTO = new PermissionDTO();
            BeanUtils.copyProperties(permission, permissionDTO);
            permissionDTOS.add(permissionDTO);
        }

        return new PageResponse<>(total, permissionDTOS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(List<String> ids) {
        QueryWrapperBuilder builder = new QueryWrapperBuilder();
        builder.in("id", ids);
        List<Permission> permissions = permissionDAO.selectByCondition(builder.build());
        for (Permission permission : permissions) {
            if (permission.getBuiltin().equals(1)) {
                throw new BusinessException("内建权限不允许被删除");
            }
        }
        permissionDAO.batchDelete(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PermissionDTO permissionDTO) {
        QueryWrapperBuilder builder = new QueryWrapperBuilder();
        builder.neq("id", permissionDTO.getId());
        builder.eq("name", permissionDTO.getName());
        List<Permission> permissions = permissionDAO.selectByCondition(builder.build());
        if (!CollectionUtils.isEmpty(permissions)) {
            throw new BusinessException("权限名不能重复");
        }
        Permission permission = permissionDAO.select(permissionDTO.getId());
        if (permission.getBuiltin().equals(1)) {
            throw new BusinessException("内建权限不允许被修改");
        }

        BeanUtils.copyProperties(permissionDTO, permission);
        permission.setTs(new Date());
        permissionDAO.update(permission);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(PermissionDTO permissionDTO) {
        QueryWrapperBuilder builder = new QueryWrapperBuilder();
        builder.eq("name", permissionDTO.getName());
        List<Permission> permissions = permissionDAO.selectByCondition(builder.build());
        if (!CollectionUtils.isEmpty(permissions)) {
            throw new BusinessException("权限名不能重复");
        }

        Permission permission = new Permission();
        BeanUtils.copyProperties(permissionDTO, permission);
        permission.setId(UUID.randomUUID().toString());
        permission.setBuiltin(0);
        permission.setTs(new Date());
        permissionDAO.insert(permission);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer count() {
        return permissionDAO.countByCondition(new QueryWrapper());
    }

}
