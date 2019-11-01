package com.unionman.webuploader.handler;

import com.unionman.webuploader.exception.ServiceException;
import com.unionman.webuploader.result.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.crypto.BadPaddingException;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.LinkedList;
import java.util.List;

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
    public JsonResult serviceExceptionHandler(ServiceException e){

        log.error("serviceExceptionHandler  {}", e.getMessage());

        return JsonResult.failMessage(e.getMessage());

    }

    /**
     * 捕捉404异常
     * @param e 404 异常
     * @date 2019/04/17 09:46:22
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public JsonResult noHandlerFoundHandle(NoHandlerFoundException e) {

        log.error("noHandlerFoundHandle {}", e.getMessage());

        return JsonResult.http404(e.getMessage());

    }

}
