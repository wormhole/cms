package net.stackoverflow.cms.web.controller.dashboard;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.model.vo.CountVO;
import net.stackoverflow.cms.security.CmsUserDetails;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
            Integer userCount = userService.count();
            Integer roleCount = roleService.count();
            Integer permissionCount = permissionService.count();
            List<CountVO> countVOs = new ArrayList<>();
            countVOs.add(new CountVO("用户", userCount));
            countVOs.add(new CountVO("角色", roleCount));
            countVOs.add(new CountVO("权限", permissionCount));

            //获取所有在线用户
            List<Object> principals = sessionRegistry.getAllPrincipals();
            List<String> users = new ArrayList<>();
            for (Object object : principals) {
                CmsUserDetails userDetails = (CmsUserDetails) object;
                users.add(userDetails.getUsername());
            }

            Map<String, Object> systemMap = new HashMap<>(16);
            //cpu使用率
            Sigar sigar = new Sigar();
            CpuPerc[] cpuPercs = sigar.getCpuPercList();
            double combined = 0D;
            for (CpuPerc cpuPerc : cpuPercs) {
                combined += cpuPerc.getCombined();
            }
            systemMap.put("cpuPercent", doubleFormat(combined / cpuPercs.length * 100) + "%");
            //内存信息
            Mem mem = sigar.getMem();
            systemMap.put("memTotal", doubleFormat(mem.getTotal() / (1024D * 1024D * 1024D)) + "GB");
            systemMap.put("memUsed", doubleFormat(mem.getUsed() / (1024D * 1024D * 1024D)) + "GB");
            systemMap.put("memUsedPercent", doubleFormat(mem.getUsedPercent()) + "%");
            //磁盘信息
            FileSystem[] fileSystems = sigar.getFileSystemList();
            FileSystemUsage usage = null;
            double diskTotal = 0D;
            double diskUsed = 0D;
            for (FileSystem fileSystem : fileSystems) {
                usage = sigar.getFileSystemUsage(fileSystem.getDirName());
                diskTotal += usage.getTotal() / (1024D * 1024D);
                diskUsed += usage.getUsed() / (1024D * 1024D);
            }
            double diskUsedPercent = diskUsed / diskTotal;
            systemMap.put("diskTotal", doubleFormat(diskTotal) + "GB");
            systemMap.put("diskUsed", doubleFormat(diskUsed) + "GB");
            systemMap.put("diskUsedPercent", doubleFormat(diskUsedPercent * 100) + "%");
            //流量信息
            double upload = 0D;
            double download = 0D;
            String[] ifNames = sigar.getNetInterfaceList();
            for (String ifName : ifNames) {
                NetInterfaceStat nfs = sigar.getNetInterfaceStat(ifName);
                upload += nfs.getTxBytes() / (1024D * 1024D * 1024D);
                download += nfs.getRxBytes() / (1024D * 1024D * 1024D);
            }
            systemMap.put("netSend", doubleFormat(upload) + "GB");
            systemMap.put("netReceive", doubleFormat(download) + "GB");

            map.put("count", countVOs);
            map.put("online", users);
            map.put("system", systemMap);

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
