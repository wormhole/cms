package net.stackoverflow.cms.model.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Config implements Serializable {

    private static final long serialVersionUID = -1392313642367798647L;
    private String id;
    private String key;
    private String value;
}
