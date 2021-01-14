package cn.darkjrong.webuploader.service;

import cn.darkjrong.webuploader.domain.MultipartFileMerge;
import cn.darkjrong.webuploader.domain.MultipartFileParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
     */
    void merge(MultipartFileMerge multipartFileMerge);

    /**
     *  非分片上传文件
     * @param file 文件
     * @date 2019/11/01 08:41:22
     */
    void oldUpload(MultipartFile file);

    /**
     *  断点，秒传
     * @param param 上传文件参数信息
     * @date 2021/01/14
     */
    void breakpointUploadFileRandomAccessFile(MultipartFileParam param);

    /**
     *  断点，秒传
     * 处理文件分块，基于MappedByteBuffer来实现文件的保存
     *
     * @param param 上传文件参数信息
     */
    void breakpointUploadFileByMappedByteBuffer(MultipartFileParam param);

    /**
     *  秒传判断，断点判断
     * @param md5 文件md5值
     * @return 未上传的分片
     */
    List<Integer> checkFileMd5(String md5);










}
