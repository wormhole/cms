package net.stackoverflow.cms.model.vo;

import lombok.*;

/**
 * 密码VO类
 *
 * @author 凉衫薄
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PasswordVO {

    private String id;
    private String oldPassword;
    private String newPassword;
    private String checkPassword;
}
