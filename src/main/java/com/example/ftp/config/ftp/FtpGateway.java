package com.example.ftp.config.ftp;

import com.example.ftp.payload.UploadFile;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
public interface FtpGateway {

    @Gateway(requestChannel = "ftpChannel")
    void upload(UploadFile uploadFile);


}
