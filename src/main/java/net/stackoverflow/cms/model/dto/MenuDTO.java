package net.stackoverflow.cms.model.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MenuDTO {

    private String id;
    private String title;
    private String key;
    private List<MenuDTO> children;
}
