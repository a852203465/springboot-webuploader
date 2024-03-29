package cn.darkjrong.webuploader.common.enums;

import org.springframework.http.HttpStatus;

/**
 *数据信息状态枚举类
 * @author Rong.Jia
 * @date 2019/4/2
 */
public enum ResponseEnum {


    // 成功
    SUCCESS(0,"成功"),

    // 失败
    ERROR(-1, "失败"),
    PARAMETER_ERROR(1, "参数不正确"),
    FILE_LIMIT_EXCEEDED(-1, "文件超出限制, 请选择较小文件"),
    SYSTEM_ERROR(-1, "系统错误"),

    PARAMS_VALIDATE_FAIL(400, "参数校验失败"),
    NOT_FOUND(HttpStatus.NOT_FOUND.value(), "请求接口不存在"),
    REQUEST_MODE_ERROR(HttpStatus.METHOD_NOT_ALLOWED.value(), "请求方式错误, 请检查"),
    MEDIA_TYPE_NOT_SUPPORTED(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "媒体类型不支持"),

    FILE_MERGE_FAILED(1001, "文件合并失败"),
    SHARD_UPLOAD_FAILED(1002, "分片上传失败"),
    FILE_UPLOAD_FAILED(1003, "文件上传失败"),
    DOWNLOAD_FAILED(1004, "下载失败"),
    NOT_A_DIRECTORY(1005, "不是目录"),
    FILE_NOT_EXIST(1006, "文件不存在"),
    REQUEST_PARAMETER_FORMAT_IS_INCORRECT(1007, "请求参数格式不正确"),
    THE_PARAMETER_TYPE_IS_INCORRECT(1008, "参数类型不正确"),
    THE_SHARDING_FILE_CANNOT_BE_EMPTY(1009, "分片文件不能为空"),
    MD5_VALIDATION_FAILED(1010, "MD5 validation failed"),
    NO_HAVE(1011, "该文件没有上传过"),
    IS_HAVE(1012, "文件已存在"),
    ING_HAVE(1013, "该文件上传了一部分"),












    ;

    /**
     * 响应状态码
     */
    private Integer code;

    /**
     * 响应描述
     */
    private String message;

    ResponseEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }


}
