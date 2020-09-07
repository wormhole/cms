package net.stackoverflow.cms.web.controller.dashboard;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.model.dto.CountDTO;
import net.stackoverflow.cms.model.dto.UserStatusDTO;
import net.stackoverflow.cms.service.DashboardService;
import net.stackoverflow.cms.util.FormatUtils;
import org.hyperic.sigar.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

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
     * 登录ip排行
     *
     * @return
     */
    @GetMapping("/top_ip")
    public ResponseEntity<Result<Map<String, Integer>>> topIp() {
        Map<String, Integer> ret = dashboardService.topIp();
        return ResponseEntity.status(HttpStatus.OK).body(Result.success(ret));
    }

    /**
     * 获取仪表盘信息
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<Result<Map<String, Object>>> dashboard() throws SigarException {
        Map<String, Object> map = new HashMap<>(16);

        //cpu使用率
        Sigar sigar = new Sigar();
        Map<String, Object> cpuMap = new HashMap<>(16);
        CpuPerc[] cpuPercs = sigar.getCpuPercList();
        double combined = 0D;
        for (CpuPerc cpuPerc : cpuPercs) {
            combined += cpuPerc.getCombined();
        }
        double used = FormatUtils.doubleFormat(combined / cpuPercs.length * 100, 2);
        double free = 100 - used;
        cpuMap.put("percent", FormatUtils.doubleFormat(combined / cpuPercs.length * 100, 2) + "%");
        cpuMap.put("count", cpuPercs.length);
        cpuMap.put("used", used);
        cpuMap.put("free", free);

        //内存信息
        Map<String, Object> memMap = new HashMap<>(16);
        Mem mem = sigar.getMem();
        memMap.put("total", FormatUtils.doubleFormat(mem.getTotal() / (1024D * 1024D * 1024D), 2));
        memMap.put("used", FormatUtils.doubleFormat(mem.getUsed() / (1024D * 1024D * 1024D), 2));
        memMap.put("free", FormatUtils.doubleFormat(mem.getFree() / (1024D * 1024D * 1024D), 2));
        memMap.put("percent", FormatUtils.doubleFormat(mem.getUsedPercent(), 2) + "%");

        //磁盘信息
        Map<String, Object> diskMap = new HashMap<>(16);
        FileSystem[] fileSystems = sigar.getFileSystemList();
        FileSystemUsage usage = null;
        double diskTotal = 0D;
        double diskUsed = 0D;
        double diskFree = 0D;
        for (FileSystem fileSystem : fileSystems) {
            if (fileSystem.getType() == 2) {
                usage = sigar.getFileSystemUsage(fileSystem.getDirName());
                diskTotal += usage.getTotal() / (1024D * 1024D);
                diskUsed += usage.getUsed() / (1024D * 1024D);
                diskFree += usage.getFree() / (1024 * 1024);
            }
        }
        double diskUsedPercent = diskUsed / diskTotal;
        diskMap.put("total", FormatUtils.doubleFormat(diskTotal, 2));
        diskMap.put("used", FormatUtils.doubleFormat(diskUsed, 2));
        diskMap.put("free", FormatUtils.doubleFormat(diskFree, 2));
        diskMap.put("percent", FormatUtils.doubleFormat(diskUsedPercent * 100, 2) + "%");

        //网络信息
        Map<String, Object> netMap = new HashMap<>(16);
        double upload = 0D;
        double download = 0D;
        String[] ifNames = sigar.getNetInterfaceList();
        for (String ifName : ifNames) {
            NetInterfaceStat nfs = sigar.getNetInterfaceStat(ifName);
            upload += nfs.getTxBytes() / (1024D * 1024D * 1024D);
            download += nfs.getRxBytes() / (1024D * 1024D * 1024D);
        }
        netMap.put("upload", FormatUtils.doubleFormat(upload, 2));
        netMap.put("download", FormatUtils.doubleFormat(download, 2));

        map.put("cpu", cpuMap);
        map.put("mem", memMap);
        map.put("disk", diskMap);
        map.put("net", netMap);

        return ResponseEntity.status(HttpStatus.OK).body(Result.success("success", map));

    }
}
