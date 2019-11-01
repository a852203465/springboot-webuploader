package com.unionman.webuploader.exception;

import com.unionman.webuploader.enums.ExceptionEnum;
import lombok.Getter;


/**
 * 全部异常异常处理类
 * @date 2019/11/01 09:43:22
 * @author Mr.J
 */
@Getter
public class ServiceException extends RuntimeException {

    private ExceptionEnum exceptionEnum;

    public ServiceException(ExceptionEnum exceptionEnum) {
        this.exceptionEnum = exceptionEnum;
    }

}
