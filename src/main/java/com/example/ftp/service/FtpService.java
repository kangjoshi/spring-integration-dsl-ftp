package com.example.ftp.service;

import com.example.ftp.config.ftp.FtpGateway;
import com.example.ftp.payload.UploadFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FtpService {

    private final FtpGateway ftpGateway;

    FtpService(FtpGateway ftpGateway) {
        this.ftpGateway = ftpGateway;
    }

    public void upload(MultipartFile file) {
        try {
            UploadFile uploadFile = new UploadFile(file.getOriginalFilename(), file.getBytes());
            ftpGateway.upload(uploadFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
