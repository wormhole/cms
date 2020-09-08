package net.stackoverflow.cms.web.controller.dashboard;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.model.dto.CountDTO;
import net.stackoverflow.cms.model.dto.DiskInfoDTO;
import net.stackoverflow.cms.model.dto.MemInfoDTO;
import net.stackoverflow.cms.model.dto.UserStatusDTO;
import net.stackoverflow.cms.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 仪表盘接口
 *
 * @author 凉衫薄
 */
@RestController
@RequestMapping(value = "/dashboard")
@Slf4j
@Validated
public class DashboardController extends BaseController {

    @Autowired
    private DashboardService dashboardService;

    /**
     * 获取总数
     *
     * @return
     */
    @GetMapping("/count")
    public ResponseEntity<Result<CountDTO>> count() {
        CountDTO dto = dashboardService.count();
        return ResponseEntity.status(HttpStatus.OK).body(Result.success(dto));
    }

    /**
     * 统计用户状态
     *
     * @return
     */
    @GetMapping("/user_status")
    public ResponseEntity<Result<UserStatusDTO>> userStatus() {
        UserStatusDTO dto = dashboardService.userStatus();
        return ResponseEntity.status(HttpStatus.OK).body(Result.success(dto));
    }

    /**
     * 获取磁盘信息
     *
     * @return
     */
    @GetMapping("/disk")
    public ResponseEntity<Result<DiskInfoDTO>> diskInfo() {
        DiskInfoDTO dto = dashboardService.diskInfo();
        return ResponseEntity.status(HttpStatus.OK).body(Result.success(dto));
    }

    /**
     * 获取内存信息
     *
     * @return
     */
    @GetMapping("/memory")
    public ResponseEntity<Result<MemInfoDTO>> memInfo() {
        MemInfoDTO dto = dashboardService.memInfo();
        return ResponseEntity.status(HttpStatus.OK).body(Result.success(dto));
    }
}
