package io.shulie.amdb.global;

import io.shulie.amdb.common.Response;
import io.shulie.amdb.exception.AmdbExcetion;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: xingchen
 * @ClassName: GlobalExceptionHandler
 * @Package: io.shulie.amdb.global
 * @Date: 2020/10/3012:14
 * @Description:
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    public static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Throwable.class)
    @ResponseBody
    public Response<String> handlerAmdbExcetionThrowable(Throwable e) {
        logger.error("handlerAmdbExcetionThrowable" + ExceptionUtils.getStackTrace(e));
        return Response.fail(e.getMessage());
    }

    @ExceptionHandler(value = AmdbExcetion.class)
    @ResponseBody
    public Response<String> handlerAmdbExcetion(AmdbExcetion e) {
        logger.error("handlerAmdbExcetion" + ExceptionUtils.getStackTrace(e));
        return Response.fail(e.getMessage());
    }
}
