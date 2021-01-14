package cn.darkjrong.webuploader.exception;

import cn.darkjrong.webuploader.enums.ResponseEnum;
import lombok.Getter;


/**
 * 全部异常异常处理类
 * @date 2019/11/01 09:43:22
 * @author Mr.J
 */
@Getter
public class ServiceException extends RuntimeException {

    private Integer code;

    private String message;

    public ServiceException(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public ServiceException(ResponseEnum responseEnum) {
        super(responseEnum.getMessage());
        this.code = responseEnum.getCode();
        this.message = responseEnum.getMessage();
    }

    public ServiceException(ResponseEnum responseEnum, String message) {
        super(message);
        this.code = responseEnum.getCode();
        this.message = message;
    }

    public ServiceException(Integer code, String message, Throwable t) {
        super(message, t);
        this.code = code;
        this.message = message;
    }

    public ServiceException(ResponseEnum responseEnum, Throwable t) {
        super(responseEnum.getMessage(), t);
        this.code = responseEnum.getCode();
        this.message = responseEnum.getMessage();
    }

}
