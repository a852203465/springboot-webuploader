package cn.darkjrong.webuploader.repository.impl;

import cn.darkjrong.webuploader.repository.WebuploaderRepository;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 分片上传持久层 接口实现类
 * @author Rong.Jia
 * @date 2021/01/14 17:50
 */
@Repository
public class WebuploaderRepositoryImpl implements WebuploaderRepository {

    /**
     * 文件所在路径, key: 文件md5, value: 文件路径
     */
    private static final ConcurrentHashMap<String, String> FILE_MD5 = new ConcurrentHashMap<>();

    /**
     * 文件所在路径, key: 文件md5, value: 上传状态 true:完成，false：未完成
     */
    private static final ConcurrentHashMap<String, Boolean> FILE_UPLOAD_STATUS = new ConcurrentHashMap<>();

    @Override
    public Boolean getFileUploadStatusByMd5(String md5) {
        return FILE_UPLOAD_STATUS.getOrDefault(md5, Boolean.FALSE);
    }

    @Override
    public Boolean hasFileUploadStatusByMd5(String md5) {
        return FILE_UPLOAD_STATUS.containsKey(md5);
    }

    @Override
    public String getFilByMd5(String md5) {
        return FILE_MD5.getOrDefault(md5, null);
    }

    @Override
    public Boolean hasFileByMd5(String md5) {
        return FILE_MD5.containsKey(md5);
    }

    @Override
    public void saveUploadStatus(String md5, Boolean status) {
        FILE_UPLOAD_STATUS.put(md5, status);
    }

    @Override
    public void saveFile(String md5, String file) {
        FILE_MD5.put(md5, file);
    }
}
