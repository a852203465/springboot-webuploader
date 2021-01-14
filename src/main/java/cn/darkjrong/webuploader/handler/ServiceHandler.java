package cn.darkjrong.webuploader.handler;

import cn.darkjrong.webuploader.enums.ResponseEnum;
import cn.darkjrong.webuploader.exception.ServiceException;
import cn.darkjrong.webuploader.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 异常控制处理器
 * @date 2019/11/01 09:43:22
 * @author Mr.J
 */
@Slf4j
@RestControllerAdvice
public class ServiceHandler {

    /**
     * 捕获自定义异常，并返回异常数据
     * @author Rong.Jia
     * @date 2019/4/3 8:46
     */
    @ExceptionHandler(value = ServiceException.class)
    public ResponseVO serviceExceptionHandler(ServiceException e){

        log.error("serviceExceptionHandler  {}", e.getMessage());

        return ResponseVO.error(e.getMessage());

    }

    /**
     * 捕捉404异常
     * @param e 404 异常
     * @date 2019/04/17 09:46:22
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseVO noHandlerFoundHandle(NoHandlerFoundException e) {

        log.error("noHandlerFoundHandle {}", e.getMessage());

        return ResponseVO.error(ResponseEnum.NOT_FOUND);

    }

}
