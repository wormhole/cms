package net.stackoverflow.cms.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Result {

    private Integer status;

    public static class Status {
        public static final Integer SUCCESS = 0;
        public static final Integer FAILURE = 1;
    }
    private String message;
    private Object data;
}
