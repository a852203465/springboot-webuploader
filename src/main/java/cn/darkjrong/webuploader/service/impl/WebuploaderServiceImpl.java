package cn.darkjrong.webuploader.service.impl;

import cn.darkjrong.webuploader.config.FileConfig;
import cn.darkjrong.webuploader.constants.FileConstant;
import cn.darkjrong.webuploader.domain.MultipartFileMerge;
import cn.darkjrong.webuploader.domain.MultipartFileParam;
import cn.darkjrong.webuploader.enums.ResponseEnum;
import cn.darkjrong.webuploader.exception.ServiceException;
import cn.darkjrong.webuploader.repository.WebuploaderRepository;
import cn.darkjrong.webuploader.service.WebuploaderService;
import cn.darkjrong.webuploader.utils.FileUtils;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

/**
 * 分片上传业务层接口实现类
 *
 * @author Rong.Jia
 * @date 2019/11/01 08:30
 */
@Slf4j
@Service
public class WebuploaderServiceImpl implements WebuploaderService {

    @Autowired
    private FileConfig fileConfig;

    @Autowired
    private WebuploaderRepository webuploaderRepository;

    @Override
    public void upload(MultipartFileParam param) {

        try {
            Assert.notNull(param.getFile(), ResponseEnum.PARAMS_VALIDATE_FAIL.getMessage());

            if (Validator.isNull(param.getChunks()) && Validator.isNull(param.getChunk())) {
                param.setChunk(0);
            }

            File outFile = new File(fileConfig.getUploadFolder() + FileConstant.SEPARATOR + param.getGuid(), param.getChunk() + FileConstant.PART_SUFFIX);

            InputStream inputStream = param.getFile().getInputStream();

            FileUtil.writeFromStream(inputStream, outFile);
        } catch (Exception e) {
            log.error("upload {}", e.getMessage());
        }
    }

    @Override
    public void merge(MultipartFileMerge multipartFileMerge) {

        File file = new File(fileConfig.getUploadFolder() + FileConstant.SEPARATOR + multipartFileMerge.getGuid());

        if (!file.isDirectory()) {
            log.error("Is a directory");
            throw new ServiceException(ResponseEnum.NOT_A_DIRECTORY);

        }

        try {

            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                File partFile = new File(fileConfig.getUploadFolder() + FileConstant.SEPARATOR + multipartFileMerge.getFileName());

                for (int i = 0; i < files.length; i++) {

                    File s = new File(fileConfig.getUploadFolder() + FileConstant.SEPARATOR + multipartFileMerge.getGuid(), i + FileConstant.PART_SUFFIX);

                    FileOutputStream destTempfos = new FileOutputStream(partFile, Boolean.TRUE);

                    FileUtil.writeToStream(s, destTempfos);
                    destTempfos.close();
                }

                FileUtils.del(file);
            }
        } catch (Exception e) {
            log.error("merge {}", e.getMessage());
            throw new ServiceException(ResponseEnum.FILE_MERGE_FAILED);
        }

    }

    @Override
    public void oldUpload(MultipartFile file) {

        if (file.isEmpty()) {

            log.error("File does not exist");
            throw new ServiceException(ResponseEnum.FILE_NOT_EXIST);
        }

        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("YYYYMMddHHmmSS");

        String format = now.format(dateTimeFormatter);

        String path =fileConfig.getUploadFolder() + FileConstant.SEPARATOR + format;

        File fileDirty = new File(path);
        FileUtil.mkdir(fileDirty);

        File outFile = new File(path + File.separator + file.getOriginalFilename());

        try {
            FileUtil.writeFromStream(file.getInputStream(), outFile);
        }catch (Exception e) {
            log.error("oldUpload {}", e.getMessage());
            throw new ServiceException(ResponseEnum.FILE_UPLOAD_FAILED);

        }
    }

    @Override
    public void breakpointUploadFileRandomAccessFile(MultipartFileParam param) {

        String fileName = param.getName();

        String tempDirPath = fileConfig.getUploadFolder() + FileConstant.SEPARATOR + param.getMd5();
        FileUtil.mkdir(tempDirPath);

        String tempFileName = fileName + FileConstant.TMP_SUFFIX;
        File tmpFile = new File(tempDirPath, tempFileName);

        try {
            RandomAccessFile accessTmpFile = new RandomAccessFile(tmpFile, "rw");

            long offset = fileConfig.getChunkSize() * param.getChunk();

            //定位到该分片的偏移量
            accessTmpFile.seek(offset);

            //写入该分片数据
            accessTmpFile.write(param.getFile().getBytes());

            // 释放
            accessTmpFile.close();

            boolean isOk = checkAndSetUploadProgress(param, tempDirPath);
            if (isOk) {
                FileUtil.rename(tmpFile, fileName, Boolean.TRUE);
            }
        }catch (Exception e) {
            log.error("breakpointUploadFileRandomAccessFile {}", e.getMessage());
            throw new ServiceException(ResponseEnum.SHARD_UPLOAD_FAILED);
        }
    }

    @Override
    public void breakpointUploadFileByMappedByteBuffer(MultipartFileParam param) {

        String fileName = param.getName();

        String uploadDirPath = fileConfig.getUploadFolder() + FileConstant.SEPARATOR + param.getMd5();
        FileUtil.mkdir(uploadDirPath);

        String tempFileName = fileName + FileConstant.TMP_SUFFIX;
        File tmpFile = new File(uploadDirPath, tempFileName);

        try {
            RandomAccessFile tempRaf = new RandomAccessFile(tmpFile, "rw");
            FileChannel fileChannel = tempRaf.getChannel();

            //写入该分片数据
            long offset =  fileConfig.getChunkSize() * param.getChunk();
            byte[] fileData = param.getFile().getBytes();
            MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, offset, fileData.length);
            mappedByteBuffer.put(fileData);

            // 释放
            FileUtils.freedMappedByteBuffer(mappedByteBuffer);
            fileChannel.close();

            boolean isOk = checkAndSetUploadProgress(param, uploadDirPath);
            if (isOk) {
                FileUtil.rename(tmpFile, fileName, Boolean.TRUE);
            }
        }catch (Exception e) {
            log.error("breakpointUploadFileByMappedByteBuffer {}", e.getMessage());
            throw new ServiceException(ResponseEnum.SHARD_UPLOAD_FAILED);
        }
    }

    @Override
    public List<Integer> checkFileMd5(String md5) {
        if (!webuploaderRepository.hasFileUploadStatusByMd5(md5)) {
            log.error("The file has not been uploaded");
            throw new ServiceException(ResponseEnum.NO_HAVE);
        }
        if (webuploaderRepository.getFileUploadStatusByMd5(md5)) {
            log.error("File already exists");
            throw new ServiceException(ResponseEnum.IS_HAVE);
        } else {
            String value = webuploaderRepository.getFilByMd5(md5);
            if (StrUtil.isNotBlank(value)) {
                File confFile = new File(value);
                byte[] completeList = FileUtil.readBytes(confFile);
                List<Integer> missChunkList = new LinkedList<>();
                for (int i = 0; i < completeList.length; i++) {
                    if (completeList[i] != Byte.MAX_VALUE) {
                        missChunkList.add(i);
                    }
                }
                return missChunkList;
            }
        }

        log.error("MD5 validation failed");
        throw new ServiceException(ResponseEnum.MD5_VALIDATION_FAILED);
    }

    /**
     * 检查并修改文件上传进度
     *
     * @param param 上传文件参数信息
     * @param uploadDirPath 上传路径
     * @return 是否成功
     * @throws IOException 文件操作异常
     */
    private boolean checkAndSetUploadProgress(MultipartFileParam param, String uploadDirPath) throws IOException {

        String fileName = param.getName();
        File confFile = new File(uploadDirPath, fileName + FileConstant.CONF_SUFFIX);

        RandomAccessFile accessConfFile = new RandomAccessFile(confFile, "rw");

        //把该分段标记为 true 表示完成
        accessConfFile.setLength(param.getChunks());
        accessConfFile.seek(param.getChunk());
        accessConfFile.write(Byte.MAX_VALUE);

        //completeList 检查是否全部完成,如果数组里是否全部都是(全部分片都成功上传)
        byte[] completeList = FileUtil.readBytes(confFile);
        byte isComplete = Byte.MAX_VALUE;
        for (int i = 0; i < completeList.length && isComplete == Byte.MAX_VALUE; i++) {

            //与运算, 如果有部分没有完成则 isComplete 不是 Byte.MAX_VALUE
            isComplete = (byte) (isComplete & completeList[i]);
        }

        accessConfFile.close();
        if (isComplete == Byte.MAX_VALUE) {
            webuploaderRepository.saveUploadStatus(param.getMd5(), Boolean.TRUE);
            webuploaderRepository.saveFile(param.getMd5(), uploadDirPath + StrUtil.SLASH + fileName);
            return true;
        } else {
            if (!webuploaderRepository.hasFileUploadStatusByMd5(param.getMd5())) {
                webuploaderRepository.saveUploadStatus(param.getMd5(), Boolean.FALSE);
            }
            if (!webuploaderRepository.hasFileByMd5(param.getMd5())) {
                webuploaderRepository.saveFile(param.getMd5(), uploadDirPath + StrUtil.SLASH + fileName + FileConstant.CONF_SUFFIX);
            }
            return false;
        }
    }




}
