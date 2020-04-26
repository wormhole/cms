package net.stackoverflow.cms.common;

import lombok.*;

/**
 * 公共返回结果
 *
 * @author 凉衫薄
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Result {

    private Boolean status;

    public static class Status {
        public static final Boolean SUCCESS = true;
        public static final Boolean FAILURE = false;
    }

    private String message;
    private Object data;
}
