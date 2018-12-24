package com.example.ftp.config.ftp;

import com.example.ftp.payload.UploadFile;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.file.remote.FileInfo;

import java.io.File;
import java.io.InputStream;
import java.util.List;

@MessagingGateway
public interface FtpGateway {


    @Gateway(requestChannel = "ftpUpload")
    void upload(UploadFile uploadFile);

    @Gateway(requestChannel = "ftpList")
    List<FileInfo> list(String dir);

    @Gateway(requestChannel = "ftpDelete")
    Boolean delete(String filePath);

    @Gateway(requestChannel = "ftpGet")
    InputStream get(String filePath);


}
