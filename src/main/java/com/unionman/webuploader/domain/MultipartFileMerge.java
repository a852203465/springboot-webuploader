package com.unionman.webuploader.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 多部分文件上传完成，合并参数对象
 * @date 2018/12/04 11:12
 * @author Rong.Jia
 */
@Data
public class MultipartFileMerge implements Serializable {

    private static final long serialVersionUID = 1775164829700280189L;

    private String guid;

    /**
     * 文件名
     */
    private String fileName;

}
