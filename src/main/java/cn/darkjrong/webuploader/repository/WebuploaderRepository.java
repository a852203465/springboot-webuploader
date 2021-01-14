package cn.darkjrong.webuploader.repository;

/**
 * 分片上传持久层 接口
 * @author Rong.Jia
 * @date 2021/01/14 17:50
 */
public interface WebuploaderRepository {

    /**
     * 根据MD5 获取 上传状态
     * @param md5 MD5值
     * @date 2021/01/14 17:50
     * @return 上传状态 true:完成，false：未完成
     */
    Boolean getFileUploadStatusByMd5(String md5);

    /**
     * 根据MD5 判断是否存在
     * @param md5 MD5值
     * @date 2021/01/14 17:50
     * @return 是否存在
     */
    Boolean hasFileUploadStatusByMd5(String md5);

    /**
     * 根据MD5 获取文件上传信息
     * @param md5 MD5值
     * @date 2021/01/14 17:50
     * @return 文件上传信息
     */
    String getFilByMd5(String md5);

    /**
     * 根据MD5 判断是否存在
     * @param md5 MD5值
     * @date 2021/01/14 17:50
     * @return 是否存在
     */
    Boolean hasFileByMd5(String md5);

    /**
     * 保存上传状态
     * @param md5  MD5值
     * @date 2021/01/14 17:50
     * @param status 上传状态 true:完成，false：未完成
     */
    void saveUploadStatus(String md5, Boolean status);

    /**
     * 保存上传文件
     * @param md5  MD5值
     * @date 2021/01/14 17:50
     * @param file 文件完整名
     */
    void saveFile(String md5, String file);









}
