package com.unionman.webuploader.controller;

import com.unionman.webuploader.domain.MultipartFileMerge;
import com.unionman.webuploader.domain.MultipartFileParam;
import com.unionman.webuploader.enums.ExceptionEnum;
import com.unionman.webuploader.exception.ServiceException;
import com.unionman.webuploader.result.JsonResult;
import com.unionman.webuploader.service.WebuploaderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 分片上传controller 层
 * @author Mr.J
 * @date 2019/10/31 22:07
 */
@Slf4j
@Controller
public class WebuploaderController {

    @Autowired
    private WebuploaderService webuploaderService;

    /**
     * 分片上传
     */
    @PostMapping("/upload")
    @ResponseBody
    public JsonResult upload(MultipartFileParam param, HttpServletRequest request) {

        log.info("upload guid {}, chunks {}, chunk {}", param.getGuid(), param.getChunks(), param.getChunk());

        boolean isMultipart = ServletFileUpload.isMultipartContent(request);

        if (isMultipart) {

            webuploaderService.upload(param);
            return JsonResult.success();

        }

        return JsonResult.fail();
    }


    /**
     * 合并所有分片
     */
    @PostMapping("/merge")
    @ResponseBody
    public JsonResult merge(@RequestBody MultipartFileMerge multipartFileMerge) {

        log.info("merge {}", multipartFileMerge.toString());

        return webuploaderService.merge(multipartFileMerge);
    }

    /**
     * 非分片上传
     *
     * @param file    file
     */
    @PostMapping("/oldUpload")
    @ResponseBody
    public JsonResult oldUpload(@RequestParam(value = "file", required = true) MultipartFile file) {

        return webuploaderService.oldUpload(file);
    }










}
