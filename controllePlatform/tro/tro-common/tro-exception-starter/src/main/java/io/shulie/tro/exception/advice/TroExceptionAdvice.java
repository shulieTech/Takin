package io.shulie.tro.exception.advice;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.shulie.tro.common.beans.response.ResponseResult;
import io.shulie.tro.exception.entity.TroException;
import io.shulie.tro.exception.holder.ExceptionMessageHolder;
import io.shulie.tro.exception.holder.ExceptionMessageHolder.ExceptionEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * @author shiyajian
 * create: 2020-09-25
 */
@RestControllerAdvice
@Slf4j
public class TroExceptionAdvice {

    @Autowired
    private ExceptionMessageHolder exceptionMessageHolder;

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseResult<Void> troExceptionHandler(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        String uri = request.getRequestURI();
        log.error("URI:[{}]参数格式不匹配，错误信息：{}", uri, e.getMessage());
        return ResponseResult.fail(HttpStatus.BAD_REQUEST.value() + "", "错误的请求参数，参数类型不匹配", "检查参数类型");
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public ResponseResult<Void> troExceptionHandler(ServletRequestBindingException e, HttpServletRequest request) {
        String uri = request.getRequestURI();
        log.error("URI:[{}]参数校验报错，错误信息：{}", uri, e.getMessage());
        return ResponseResult.fail(HttpStatus.BAD_REQUEST.value() + "", "错误的请求参数，参数校验不通过", "检查参数的校验规则");
    }

    @ExceptionHandler(TroException.class)
    public ResponseResult<Void> troExceptionHandler(TroException e) {
        ExceptionEntity exceptionEntity = exceptionMessageHolder.get(e.getEx(), e.getSource());
        return exceptionResolve(e, exceptionEntity);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseResult<Void> exceptionHandler(Throwable e) {
        ExceptionEntity exceptionEntity = exceptionMessageHolder.defaultException();
        String message = "";
        if (e.getLocalizedMessage() != null) {
            message = e.getLocalizedMessage();
        }
        if (exceptionEntity.getMessage() != null) {
            message = e.getMessage();
        }
        exceptionEntity.setMessage(message);
        return exceptionResolve(e, exceptionEntity);
    }

    private ResponseResult<Void> exceptionResolve(Throwable e, ExceptionEntity exceptionEntity) {
        log.error(exceptionEntity.getMessage() + "," + exceptionEntity.getSolution(), e);
        ResponseResult<Void> fail = ResponseResult.fail(exceptionEntity.getCode(), exceptionEntity.getMessage(),
            exceptionEntity.getSolution());
        if (exceptionEntity.getHttpStatus() != HttpStatus.OK) {
            HttpServletResponse response = ((ServletRequestAttributes)Objects.requireNonNull(RequestContextHolder
                .getRequestAttributes())).getResponse();
            assert response != null;
            response.setStatus(exceptionEntity.getHttpStatus().value());
        }
        return fail;
    }
}
