package cn.darkjrong.webuploader.service.impl;

import cn.darkjrong.webuploader.constants.FileConstant;
import cn.darkjrong.webuploader.domain.MultipartFileMerge;
import cn.darkjrong.webuploader.domain.MultipartFileParam;
import cn.darkjrong.webuploader.enums.ResponseEnum;
import cn.darkjrong.webuploader.exception.ServiceException;
import cn.darkjrong.webuploader.service.WebuploaderService;
import cn.darkjrong.webuploader.utils.FileUtils;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 分片上传业务层接口实现类
 *
 * @author Rong.Jia
 * @date 2019/11/01 08:30
 */
@Slf4j
@Service
public class WebuploaderServiceImpl implements WebuploaderService {

    @Override
    public void upload(MultipartFileParam param) {

        try {
            Assert.notNull(param.getFile(), ResponseEnum.PARAMS_VALIDATE_FAIL.getMessage());

            if (Validator.isNull(param.getChunks()) && Validator.isNull(param.getChunk())) {
                param.setChunk(0);
            }

            File outFile = new File(FileConstant.FILE_DIR + FileConstant.SEPARATOR + param.getGuid(), param.getChunk() + FileConstant.PART_SUFFIX);

            InputStream inputStream = param.getFile().getInputStream();

            FileUtil.writeFromStream(inputStream, outFile);
        } catch (Exception e) {
            log.error("upload {}", e.getMessage());
        }
    }

    @Override
    public void merge(MultipartFileMerge multipartFileMerge) {

        File file = new File(FileConstant.FILE_DIR + FileConstant.SEPARATOR + multipartFileMerge.getGuid());

        if (!file.isDirectory()) {
            log.error("Is a directory");
            throw new ServiceException(ResponseEnum.NOT_A_DIRECTORY);

        }

        try {

            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                File partFile = new File(FileConstant.FILE_DIR + FileConstant.SEPARATOR + multipartFileMerge.getFileName());

                for (int i = 0; i < files.length; i++) {

                    File s = new File(FileConstant.FILE_DIR + FileConstant.SEPARATOR + multipartFileMerge.getGuid(), i + FileConstant.PART_SUFFIX);

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

        String path =FileConstant.FILE_DIR + FileConstant.SEPARATOR + format;

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
}
