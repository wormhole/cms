package net.stackoverflow.cms.web.controller;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.BaseController;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.model.dto.MenuDTO;
import net.stackoverflow.cms.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 菜单管理模块
 *
 * @author 凉衫薄
 */
@RestController
@RequestMapping(value = "/menu")
@Slf4j
@Validated
public class MenuController extends BaseController {

    @Autowired
    private MenuService menuService;

    /**
     * 获取菜单树
     *
     * @return
     */
    @GetMapping("/tree")
    public ResponseEntity<Result<List<MenuDTO>>> queryTree() {
        List<MenuDTO> dtos = menuService.findTree();
        return ResponseEntity.status(HttpStatus.OK).body(Result.success(dtos));
    }

    /**
     * 获取菜单权限
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<Result<List<String>>> queryAll() {
        List<String> menus = menuService.findKeysByUserId(super.getUserId());
        return ResponseEntity.status(HttpStatus.OK).body(Result.success(menus));
    }
}
