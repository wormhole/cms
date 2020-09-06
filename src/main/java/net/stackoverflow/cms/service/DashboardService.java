package net.stackoverflow.cms.service;

import net.stackoverflow.cms.model.dto.CountDTO;

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
}
