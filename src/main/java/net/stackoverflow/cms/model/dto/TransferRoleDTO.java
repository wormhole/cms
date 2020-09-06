package net.stackoverflow.cms.model.dto;

import lombok.*;

import java.util.List;

/**
 * 角色穿梭框
 *
 * @author 凉衫薄
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TransferRoleDTO {

    private List<RoleDTO> all;
    private List<RoleDTO> target;
}
