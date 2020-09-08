package net.stackoverflow.cms.service;

import net.stackoverflow.cms.model.dto.CountDTO;
import net.stackoverflow.cms.model.dto.DiskInfoDTO;
import net.stackoverflow.cms.model.dto.MemInfoDTO;
import net.stackoverflow.cms.model.dto.UserStatusDTO;

/**
 * 首页服务接口
 *
 * @author 凉衫薄
 */
public interface DashboardService {

    /**
     * 获取统计数量
     *
     * @return
     */
    CountDTO count();

    /**
     * 用户在线状态统计
     *
     * @return
     */
    UserStatusDTO userStatus();

    /**
     * 获取磁盘信息
     *
     * @return
     */
    DiskInfoDTO diskInfo();

    /**
     * 获取内存信息
     *
     * @return
     */
    MemInfoDTO memInfo();
}
