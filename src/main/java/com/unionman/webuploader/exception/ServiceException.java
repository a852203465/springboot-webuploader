package com.unionman.webuploader.exception;

import com.unionman.webuploader.enums.ExceptionEnum;
import lombok.Getter;


/**
 * 全部异常异常处理类
 */
@Getter
public class ServiceException extends RuntimeException {

    private ExceptionEnum exceptionEnum;

    public ServiceException(ExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
    }

}
