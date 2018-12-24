package com.example.ftp.controller;

import com.example.ftp.service.FtpService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/")
public class FtpController {

    private final FtpService ftpService;

    public FtpController(FtpService ftpService) {
        this.ftpService = ftpService;
    }

    @PostMapping
    public void upload(@RequestParam() MultipartFile file) {
        ftpService.upload(file);
    }

    @GetMapping
    public void list(@RequestParam String dir) {
        ftpService.list(dir);
    }

    @DeleteMapping
    public void delete(@RequestParam String filePath) {
        ftpService.delete(filePath);
    }

    @GetMapping("/get")
    public void get(@RequestParam String filePath) {
        ftpService.get(filePath);
    }

}
