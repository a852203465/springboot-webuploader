package com.unionman.webuploader.service.impl;

import com.unionman.webuploader.constants.FileConstant;
import com.unionman.webuploader.domain.MultipartFileMerge;
import com.unionman.webuploader.domain.MultipartFileParam;
import com.unionman.webuploader.enums.ExceptionEnum;
import com.unionman.webuploader.exception.ServiceException;
import com.unionman.webuploader.result.JsonResult;
import com.unionman.webuploader.service.WebuploaderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.unionman.webuploader.constants.FileConstant.PART_SUFFIX;

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
            if (param.getFile() == null) {
                throw new ServiceException(ExceptionEnum.PARAMS_VALIDATE_FAIL);
            }

            if (param.getChunks() == null && param.getChunk() == null) {
                param.setChunk(0);
            }

            File outFile = new File(FileConstant.FILE_DIR + FileConstant.SEPARATOR + param.getGuid(), param.getChunk() + PART_SUFFIX);

            InputStream inputStream = param.getFile().getInputStream();

            FileUtils.copyInputStreamToFile(inputStream, outFile);

        } catch (Exception e) {
            log.error("upload {}", e.getMessage());
        }
    }

    @Override
    public JsonResult merge(MultipartFileMerge multipartFileMerge) {

        File file = new File(FileConstant.FILE_DIR + FileConstant.SEPARATOR + multipartFileMerge.getGuid());

        if (!file.isDirectory()) {
            log.error("Is a directory");
            throw new ServiceException(ExceptionEnum.NOT_A_DIRECTORY);

        }

        try {

            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                File partFile = new File(FileConstant.FILE_DIR + FileConstant.SEPARATOR + multipartFileMerge.getFileName());

                for (int i = 0; i < files.length; i++) {

                    File s = new File(FileConstant.FILE_DIR + FileConstant.SEPARATOR + multipartFileMerge.getGuid(), i + PART_SUFFIX);

                    FileOutputStream destTempfos = new FileOutputStream(partFile, Boolean.TRUE);
                    FileUtils.copyFile(s, destTempfos);
                    destTempfos.close();
                }

                FileUtils.deleteDirectory(file);
            }

            return JsonResult.success();
        } catch (Exception e) {
            log.error("merge {}", e.getMessage());
            return JsonResult.fail();

        }

    }

    @Override
    public JsonResult oldUpload(MultipartFile file) {

        if (file.isEmpty()) {

            log.error("File does not exist");
            throw new ServiceException(ExceptionEnum.FILE_NOT_EXIST);
        }

        LocalDateTime now = LocalDateTime.now();

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("YYYYMMddHHmmSS");

        String format = now.format(dateTimeFormatter);

        String path =FileConstant.FILE_DIR + FileConstant.SEPARATOR + format;

        File fileDirty = new File(path);
        if (!fileDirty.exists()) {
            fileDirty.mkdirs();
        }

        File outFile = new File(path + File.separator + file.getOriginalFilename());

        try {
            FileUtils.copyInputStreamToFile(file.getInputStream(), outFile);
            return JsonResult.success();

        }catch (Exception e) {
            log.error("oldUpload {}", e.getMessage());
            return JsonResult.fail();

        }
    }
}