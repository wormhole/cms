package net.stackoverflow.cms.service;

import net.stackoverflow.cms.model.dto.CountDTO;
import net.stackoverflow.cms.model.dto.DiskInfoDTO;
import net.stackoverflow.cms.model.dto.UserStatusDTO;

import java.util.Map;

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
     * 登录地址排行
     *
     * @return
     */
    Map<String, Integer> topIp();

    /**
     * 获取磁盘信息
     *
     * @return
     */
    DiskInfoDTO diskInfo();
}
