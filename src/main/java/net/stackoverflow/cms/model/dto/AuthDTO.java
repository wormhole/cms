package net.stackoverflow.cms.model.dto;

import lombok.*;

import java.util.List;

/**
 * 用户和菜单信息dto类
 *
 * @author 凉衫薄
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuthDTO {

    private String username;
    private List<String> menus;
}
