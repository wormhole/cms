package net.stackoverflow.cms.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 公共返回结果
 *
 * @author 凉衫薄
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Result {

    private Boolean status;

    public static class Status {
        public static final Boolean SUCCESS = true;
        public static final Boolean FAILURE = false;
    }

    private String message;
    private Object data;
}
