package net.stackoverflow.cms.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UploadDTO {

    private String id;
    private String name;
    private String path;
    private String userId;
    private Integer type;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date ts;

    private String url;
    private String username;
}
