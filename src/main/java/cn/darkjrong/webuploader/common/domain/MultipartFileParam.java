package cn.darkjrong.webuploader.common.domain;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 *  文件上传 分片 vo对象
 * @date 2018/10/26 09:43
 * @author Rong.Jia
 */
@Data
public class MultipartFileParam implements Serializable {

    private static final long serialVersionUID = -839158684173491776L;

    /**
     * 任务ID
     */
    private String guid;

    /**
     * 总分片数量
     */
    private Integer chunks;

    /**
     * 当前为第几块分片
     */
    private Integer chunk;

    /**
     * 分片对象
     */
    private MultipartFile file;

    /**
     * MD5
     */
    private String md5;

    /**
     * 文件名
     */
    private String name;




}
