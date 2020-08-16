package net.stackoverflow.cms.web.controller;

import lombok.extern.slf4j.Slf4j;
import net.stackoverflow.cms.common.Result;
import net.stackoverflow.cms.exception.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 全局异常处理
 *
 * @author 凉衫薄
 */
@RestControllerAdvice
@Slf4j
public class ExceptionController {

    /**
     * 方法入参校验
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<Result<Object>> ConstraintViolationException(ConstraintViolationException e) {
        log.error(e.getMessage(), e);
        StringBuilder sb = new StringBuilder();
        e.getConstraintViolations().forEach(violation -> {
            sb.append(violation.getMessageTemplate() + "，");
        });

        return ResponseEntity.status(HttpStatus.OK).body(Result.failure(sb.substring(0, sb.length() - 1)));
    }

    /**
     * 属性校验
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Result<Map<String, String>>> MethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        Map<String, String> map = new HashMap<>(16);
        BindingResult br = e.getBindingResult();
        List<FieldError> fes = br.getFieldErrors();
        fes.forEach(fe -> {
            map.put(fe.getField(), fe.getDefaultMessage());
        });
        return ResponseEntity.status(HttpStatus.OK).body(Result.failure("字段校验失败", map));
    }

    /**
     * 业务异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<Result<Object>> businessException(BusinessException e) {
        log.error(e.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(Result.failure(e.getMessage(), e.getData()));
    }

    /**
     * 其他运行时异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Result<Object>> exception(Exception e) {
        log.error(e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Result.failure(e.getMessage()));
    }
}
