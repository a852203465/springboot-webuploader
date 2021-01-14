package cn.darkjrong.webuploader.controller;

import cn.darkjrong.webuploader.service.WebuploaderService;
import cn.darkjrong.webuploader.domain.MultipartFileMerge;
import cn.darkjrong.webuploader.domain.MultipartFileParam;
import cn.darkjrong.webuploader.enums.ResponseEnum;
import cn.darkjrong.webuploader.vo.ResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

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

    /**
     * 秒传判断，断点判断
     * @param  md5 md5
     * @return
     */
    @RequestMapping(value = "/checkFileMd5", method = RequestMethod.POST)
    @ResponseBody
    public ResponseVO checkFileMd5(String md5) {

        log.info("checkFileMd5 {}", md5);

        List<Integer> checkFileMd5 = webuploaderService.checkFileMd5(md5);

        return ResponseVO.error(ResponseEnum.ING_HAVE, checkFileMd5);
    }

    /**
     * 秒传，断点 上传
     */
    @RequestMapping(value = "/breakpointUpload", method = RequestMethod.POST)
    @ResponseBody
    public ResponseVO breakpointUpload(MultipartFileParam param, HttpServletRequest request) {

        boolean isMultipart = ServletFileUpload.isMultipartContent(request);

        if (isMultipart) {
            // 方法1
//            webuploaderService.breakpointUploadFileRandomAccessFile(param);

            // 方法2
             webuploaderService.breakpointUploadFileByMappedByteBuffer(param);

            return ResponseVO.success();
        }

        return ResponseVO.error(ResponseEnum.SHARD_UPLOAD_FAILED);
    }








}
