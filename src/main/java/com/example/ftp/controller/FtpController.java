package com.example.ftp.controller;

import com.example.ftp.service.FtpService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FtpController {

    private final FtpService ftpService;

    public FtpController(FtpService ftpService) {
        this.ftpService = ftpService;
    }

    @PostMapping("/")
    public String upload(@RequestParam() MultipartFile file) {

        ftpService.upload(file);

        return "OK";
    }



}
