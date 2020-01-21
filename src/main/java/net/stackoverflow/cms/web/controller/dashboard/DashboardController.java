package net.stackoverflow.cms.web.controller.dashboard;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.service.PermissionService;
import net.stackoverflow.cms.service.RoleService;
import net.stackoverflow.cms.service.UserService;
import org.hyperic.sigar.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.session.SessionRegistry;
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
public class DashboardController extends BaseController {

    @Autowired
    private SessionRegistry sessionRegistry;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;

    /**
     * 获取仪表盘信息
     *
     * @return
     */
    @GetMapping(value = "/info")
    public ResponseEntity info() {
        Result result = new Result();
        try {
            Map<String, Object> map = new HashMap<>(16);

            //获取用户,角色,权限数量
            Map<String, Integer> countMap = new HashMap<>(16);
            Integer userCount = userService.count();
            Integer roleCount = roleService.count();
            Integer permissionCount = permissionService.count();
            Integer onlineCount = sessionRegistry.getAllPrincipals().size();
            countMap.put("user", userCount);
            countMap.put("role", roleCount);
            countMap.put("permission", permissionCount);
            countMap.put("online", onlineCount);

            //cpu使用率
            Sigar sigar = new Sigar();
            Map<String, Object> cpuMap = new HashMap<>(16);
            CpuPerc[] cpuPercs = sigar.getCpuPercList();
            double combined = 0D;
            for (CpuPerc cpuPerc : cpuPercs) {
                combined += cpuPerc.getCombined();
            }
            double used = doubleFormat(combined / cpuPercs.length * 100);
            double free = 100 - used;
            cpuMap.put("percent", doubleFormat(combined / cpuPercs.length * 100) + "%");
            cpuMap.put("count", cpuPercs.length);
            cpuMap.put("used", used);
            cpuMap.put("free", free);

            //内存信息
            Map<String, Object> memMap = new HashMap<>(16);
            Mem mem = sigar.getMem();
            memMap.put("total", doubleFormat(mem.getTotal() / (1024D * 1024D * 1024D)));
            memMap.put("used", doubleFormat(mem.getUsed() / (1024D * 1024D * 1024D)));
            memMap.put("free", doubleFormat(mem.getFree() / (1024D * 1024D * 1024D)));
            memMap.put("percent", doubleFormat(mem.getUsedPercent()) + "%");

            //磁盘信息
            Map<String, Object> diskMap = new HashMap<>(16);
            FileSystem[] fileSystems = sigar.getFileSystemList();
            FileSystemUsage usage = null;
            double diskTotal = 0D;
            double diskUsed = 0D;
            double diskFree = 0D;
            for (FileSystem fileSystem : fileSystems) {
                usage = sigar.getFileSystemUsage(fileSystem.getDirName());
                diskTotal += usage.getTotal() / (1024D * 1024D);
                diskUsed += usage.getUsed() / (1024D * 1024D);
                diskFree += usage.getFree() / (1024 * 1024);
            }
            double diskUsedPercent = diskUsed / diskTotal;
            diskMap.put("total", doubleFormat(diskTotal));
            diskMap.put("used", doubleFormat(diskUsed));
            diskMap.put("free", doubleFormat(diskFree));
            diskMap.put("percent", doubleFormat(diskUsedPercent * 100) + "%");

            //网络信息
            Map<String, Object> netMap = new HashMap<>();
            double upload = 0D;
            double download = 0D;
            String[] ifNames = sigar.getNetInterfaceList();
            for (String ifName : ifNames) {
                NetInterfaceStat nfs = sigar.getNetInterfaceStat(ifName);
                upload += nfs.getTxBytes() / (1024D * 1024D * 1024D);
                download += nfs.getRxBytes() / (1024D * 1024D * 1024D);
            }
            netMap.put("upload", doubleFormat(upload) + "GB");
            netMap.put("download", doubleFormat(download) + "GB");

            map.put("count", countMap);
            map.put("cpu", cpuMap);
            map.put("mem", memMap);
            map.put("disk", diskMap);
            map.put("net", netMap);

            result.setMessage("success");
            result.setData(map);
            result.setStatus(Result.Status.SUCCESS);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (Exception e) {
            log.error(e.getMessage());
            result.setStatus(Result.Status.FAILURE);
            result.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }
}
