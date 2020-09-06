package net.stackoverflow.cms.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.model.dto.CountDTO;
import net.stackoverflow.cms.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 首页服务实现类
 *
 * @author 凉衫薄
 */
@Service
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private UploadService uploadService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CountDTO count() {
        CountDTO dto = new CountDTO();
        Integer userCount = userService.count();
        Integer roleCount = roleService.count();
        Integer menuCount = menuService.count();
        Integer fileCount = uploadService.count();
        dto.setFile(fileCount);
        dto.setRole(roleCount);
        dto.setUser(userCount);
        dto.setMenu(menuCount);
        return dto;
    }
}
