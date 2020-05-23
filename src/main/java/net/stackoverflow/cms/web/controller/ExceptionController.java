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
    public ResponseEntity ConstraintViolationException(ConstraintViolationException e) {
        log.error(e.getMessage(), e);
        StringBuilder sb = new StringBuilder();
        e.getConstraintViolations().forEach(violation -> {
            sb.append(violation.getMessageTemplate() + "，");
        });

        Result result = new Result();
        result.setStatus(Result.Status.FAILURE);
        result.setMessage(sb.substring(0, sb.length() - 1));
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * 属性校验
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity MethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        Result result = new Result();
        Map<String, String> map = new HashMap<>();
        BindingResult br = e.getBindingResult();
        List<FieldError> fes = br.getFieldErrors();
        fes.forEach(fe -> {
            map.put(fe.getField(), fe.getDefaultMessage());
        });
        result.setStatus(Result.Status.FAILURE);
        result.setMessage("字段校验失败");
        result.setData(map);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * 业务异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity businessException(BusinessException e) {
        log.error(e.getMessage());

        Result result = new Result();
        result.setStatus(Result.Status.SUCCESS);
        result.setMessage(e.getMessage());
        result.setData(e.getData());
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * 其他运行时异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity exception(Exception e) {
        log.error(e.getMessage(), e);
        Result result = new Result();
        result.setStatus(Result.Status.FAILURE);
        result.setMessage(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
    }
}
