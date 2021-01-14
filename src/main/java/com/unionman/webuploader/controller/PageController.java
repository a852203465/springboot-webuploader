package com.unionman.webuploader.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author Mr.J
 * @date 2019/10/31 22:05
 */
@Controller
public class PageController {

    @GetMapping("/ordinary")
    public String oldupload() {
        return "oldupload";
    }

    @GetMapping("/webuploader")
    public String webuploader() {
        return "webupload";
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/breakpointUploader")
    public String breakpointUploader() {
        return "breakpointUploader";
    }









}
