package com.unionman.webuploader.controller;

import com.unionman.webuploader.domain.MultipartFileMerge;
import com.unionman.webuploader.domain.MultipartFileParam;
import com.unionman.webuploader.enums.ResponseEnum;
import com.unionman.webuploader.service.WebuploaderService;
import com.unionman.webuploader.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

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
    public ResponseVO upload(MultipartFileParam param, HttpServletRequest request) {

        log.info("upload guid {}, chunks {}, chunk {}", param.getGuid(), param.getChunks(), param.getChunk());

        boolean isMultipart = ServletFileUpload.isMultipartContent(request);

        if (isMultipart) {

            webuploaderService.upload(param);
            return ResponseVO.success();
        }

        return ResponseVO.error(ResponseEnum.SHARD_UPLOAD_FAILED);
    }


    /**
     * 合并所有分片
     */
    @PostMapping("/merge")
    @ResponseBody
    public ResponseVO merge(@RequestBody MultipartFileMerge multipartFileMerge) {

        log.info("merge {}", multipartFileMerge.toString());

        webuploaderService.merge(multipartFileMerge);

        return ResponseVO.success();
    }

    /**
     * 非分片上传
     *
     * @param file    file
     */
    @PostMapping("/oldUpload")
    @ResponseBody
    public ResponseVO oldUpload(@RequestParam(value = "file", required = true) MultipartFile file) {

        webuploaderService.oldUpload(file);

        return ResponseVO.success();
    }










}
