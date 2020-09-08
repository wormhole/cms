package net.stackoverflow.cms.service.impl;

import com.sun.management.OperatingSystemMXBean;
import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.constant.RedisPrefixConst;
import net.stackoverflow.cms.model.dto.CountDTO;
import net.stackoverflow.cms.model.dto.DiskInfoDTO;
import net.stackoverflow.cms.model.dto.MemInfoDTO;
import net.stackoverflow.cms.model.dto.UserStatusDTO;
import net.stackoverflow.cms.service.*;
import net.stackoverflow.cms.util.FormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.rmi.server.ExportException;

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
    @Autowired
    private RedisTemplate redisTemplate;

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

    @Override
    @Transactional(rollbackFor = ExportException.class)
    public UserStatusDTO userStatus() {
        UserStatusDTO dto = new UserStatusDTO();
        Integer online = redisTemplate.keys(RedisPrefixConst.TOKEN_PREFIX + "*").size();
        Integer lock = redisTemplate.keys(RedisPrefixConst.LOCK_PREFIX + "*").size();
        Integer enable = userService.countEnable();
        Integer disable = userService.countDisable();
        Integer total = userService.count();
        dto.setTotal(total);
        dto.setDisable(disable);
        dto.setEnable(enable);
        dto.setLock(lock);
        dto.setOnline(online);
        return dto;
    }

    @Override
    public DiskInfoDTO diskInfo() {
        File file = new File(".");
        Double total = FormatUtils.doubleFormat(file.getTotalSpace() / (1024.0 * 1024.0 * 1024.0), 2);
        Double free = FormatUtils.doubleFormat(file.getFreeSpace() / (1024.0 * 1024.0 * 1024.0), 2);
        Double used = FormatUtils.doubleFormat((file.getTotalSpace() - file.getFreeSpace()) / (1024.0 * 1024.0 * 1024.0), 2);
        DiskInfoDTO dto = new DiskInfoDTO(total, free, used);
        return dto;
    }

    @Override
    public MemInfoDTO memInfo() {
        OperatingSystemMXBean os = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        Double total = FormatUtils.doubleFormat(os.getTotalPhysicalMemorySize() / (1024.0 * 1024.0 * 1024.0), 2);
        Double free = FormatUtils.doubleFormat(os.getFreePhysicalMemorySize() / (1024.0 * 1024.0 * 1024.0), 2);
        Double used = FormatUtils.doubleFormat((os.getTotalPhysicalMemorySize() - os.getFreePhysicalMemorySize()) / (1024.0 * 1024.0 * 1024.0), 2);
        MemInfoDTO dto = new MemInfoDTO(total, free, used);
        return dto;
    }
}
