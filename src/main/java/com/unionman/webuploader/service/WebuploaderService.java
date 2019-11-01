package com.unionman.webuploader.service;

import com.unionman.webuploader.domain.MultipartFileMerge;
import com.unionman.webuploader.domain.MultipartFileParam;
import com.unionman.webuploader.vo.ResponseVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 分片上传业务层接口
 * @author Rong.Jia
 * @date 2019/11/01 08:30
 */
public interface WebuploaderService {

    /**
     * 分片上传文件
     * @param param 分片文件
     * @date 2019/11/01 08:41:22
     */
    void upload(MultipartFileParam param);

    /**
     * 合并文件
     * @param multipartFileMerge 合并文件信息
     * @date 2019/11/01 08:41:22
     * @return
     */
    ResponseVO merge(MultipartFileMerge multipartFileMerge);

    /**
     *  非分片上传文件
     * @param file 文件
     * @date 2019/11/01 08:41:22
     * @return
     */
    ResponseVO oldUpload(MultipartFile file);










}
