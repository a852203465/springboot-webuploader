package com.unionman.webuploader.controller;

import com.unionman.webuploader.domain.MultipartFileMerge;
import com.unionman.webuploader.domain.MultipartFileParam;
import com.unionman.webuploader.enums.ExceptionEnum;
import com.unionman.webuploader.exception.ServiceException;
import com.unionman.webuploader.result.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Mr.J
 * @date 2019/10/31 22:07
 */
@Slf4j
@Controller
public class WebuploaderController {

    /**
     * 分片上传
     * @return ResponseEntity<Void>
     */
    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<Void> upload(MultipartFileParam param, HttpServletRequest request) {

        log.info("upload guid {}, chunks {}, chunk {}", param.getGuid(), param.getChunks(), param.getChunk());

        try {
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);

            if (isMultipart) {

                if (param.getFile() == null) {
                    throw new ServiceException(ExceptionEnum.PARAMS_VALIDATE_FAIL);
                }

                if (param.getChunks() == null && param.getChunk() == null) {
                    param.setChunk(0);
                }

                File outFile = new File("data/tmp"+File.separator + param.getGuid(), param.getChunk() + ".part");

                InputStream inputStream = param.getFile().getInputStream();

                FileUtils.copyInputStreamToFile(inputStream, outFile);

                return ResponseEntity.ok().build();
            }
        } catch (Exception e) {
            log.error("Exception {}", e.getMessage());
            return null;
        }
        return null;
    }


    /**
     * 合并所有分片
     */
    @PostMapping("/merge")
    @ResponseBody
    public JsonResult merge(@RequestBody MultipartFileMerge multipartFileMerge) {

        log.info("merge {}", multipartFileMerge.toString());

        try {
            File file = new File("data/tmp"+File.separator + multipartFileMerge.getGuid());

            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null && files.length > 0) {
                    File partFile = new File("data/tmp" + File.separator + multipartFileMerge.getFileName());

                    for (int i = 0; i < files.length; i++) {
                        File s = new File("data/tmp"+File.separator + multipartFileMerge.getGuid(), i + ".part");

                        FileOutputStream destTempfos = new FileOutputStream(partFile, true);
                        FileUtils.copyFile(s,destTempfos );
                        destTempfos.close();
                    }

                    FileUtils.deleteDirectory(file);
                }
            }

            return JsonResult.success();
        }catch (Exception e) {
            log.error("merge {}", e.getMessage());
            return JsonResult.fail();
        }
    }

    /**
     * 非分片上传
     *
     * @param request request
     * @param file    file
     * @return ResponseEntity<Void>
     * @throws IOException IOException
     */
    @PostMapping("/oldupload")
    @ResponseBody
    public ResponseEntity<Void> decrypt(HttpServletRequest request, @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter yyyymMddHHmmSS = DateTimeFormatter.ofPattern("YYYYMMddHHmmSS");
        String format = now.format(yyyymMddHHmmSS);
        String path = "data/tmp" + File.separator + format;
        File fileDirty = new File(path);
        if (!fileDirty.exists()) {
            fileDirty.mkdirs();
        }
        //FILEPATH=path + File.separator + file.getOriginalFilename();
        File outFile = new File(path + File.separator + file.getOriginalFilename());
        request.setAttribute("filePath",path + File.separator + file.getOriginalFilename());
        FileUtils.copyInputStreamToFile(file.getInputStream(), outFile);
        return ResponseEntity.ok().build();
    }










}
