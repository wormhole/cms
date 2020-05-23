package net.stackoverflow.cms.web.controller.file;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.Page;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.constant.UploadConst;
import net.stackoverflow.cms.model.entity.File;
import net.stackoverflow.cms.model.vo.FileVO;
import net.stackoverflow.cms.model.vo.IdsVO;
import net.stackoverflow.cms.service.FileService;
import net.stackoverflow.cms.service.UserService;
import net.stackoverflow.cms.util.StringUtils;
import net.stackoverflow.cms.util.SysUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 图片管理模块
 *
 * @author 凉衫薄
 */
@RestController
@RequestMapping(value = "/file/image")
@Slf4j
@Validated
public class ImageController extends BaseController {

    @Autowired
    private FileService fileService;
    @Autowired
    private UserService userService;

    /**
     * 分页查询图片信息
     *
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @param key
     * @return
     */
    @GetMapping(value = "/list")
    public ResponseEntity list(
            @RequestParam(value = "page") @Min(value = 1, message = "page不能小于1") Integer page,
            @RequestParam(value = "limit") @Min(value = 1, message = "limit不能小于1") Integer limit,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "order", required = false) String order,
            @RequestParam(value = "key", required = false) String key) {

        Result result = new Result();

        Map<String, Object> resultMap = new HashMap<>(16);
        Map<String, Object> condition = new HashMap<>(16);

        if (StringUtils.isBlank(order) || StringUtils.isBlank(sort)) {
            sort = "time";
            order = "desc";
        }
        if (StringUtils.isBlank(key)) {
            key = null;
        }
        condition.put("type", 1);
        condition.put("userId", getUserDetails().getId());
        Page pageParam = new Page(page, limit, sort, order, condition, key);
        List<File> files = fileService.findByPage(pageParam);
        pageParam.setLimit(null);
        pageParam.setOffset(null);
        int total = fileService.findByPage(pageParam).size();

        List<FileVO> vos = new ArrayList<>();
        for (File file : files) {
            FileVO vo = new FileVO();
            BeanUtils.copyProperties(file, vo);
            vo.setUrl(UploadConst.PREFIX + vo.getPath());
            vo.setUsername(userService.findById(vo.getUserId()).getUsername());
            String path = SysUtils.pwd() + vo.getPath();
            if (SysUtils.isWin())
                path = path.replace("/", "\\");
            else
                path = path.replace("\\", "/");
            vo.setPath(path);
            vos.add(vo);
        }

        resultMap.put("list", vos);
        resultMap.put("total", total);

        result.setStatus(Result.Status.SUCCESS);
        result.setMessage("success");
        result.setData(resultMap);
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity delete(@RequestBody @Validated IdsVO idsVO) {
        Result result = new Result();

        fileService.batchDelete(idsVO.getIds());
        result.setStatus(Result.Status.SUCCESS);
        result.setMessage("success");
        return ResponseEntity.status(HttpStatus.OK).body(result);

    }
}
