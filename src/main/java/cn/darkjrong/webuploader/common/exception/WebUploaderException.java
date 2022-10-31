package cn.darkjrong.webuploader.common.exception;

import cn.darkjrong.webuploader.common.enums.ResponseEnum;
import lombok.Getter;


/**
 * 全部异常异常处理类
 *
 * @author Rong.Jia
 * @date 2022/10/31
 */
@Getter
public class WebUploaderException extends RuntimeException {

    private Integer code;

    private String message;

    public WebUploaderException(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public WebUploaderException(ResponseEnum responseEnum) {
        super(responseEnum.getMessage());
        this.code = responseEnum.getCode();
        this.message = responseEnum.getMessage();
    }

    public WebUploaderException(ResponseEnum responseEnum, String message) {
        super(message);
        this.code = responseEnum.getCode();
        this.message = message;
    }

    public WebUploaderException(Integer code, String message, Throwable t) {
        super(message, t);
        this.code = code;
        this.message = message;
    }

    public WebUploaderException(ResponseEnum responseEnum, Throwable t) {
        super(responseEnum.getMessage(), t);
        this.code = responseEnum.getCode();
        this.message = responseEnum.getMessage();
    }

}
