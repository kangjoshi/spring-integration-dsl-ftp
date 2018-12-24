package com.example.ftp.service;

import com.example.ftp.config.ftp.FtpGateway;
import com.example.ftp.payload.UploadFile;
import org.springframework.integration.file.remote.FileInfo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

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


    public void list(String path) {

        List<FileInfo> files = ftpGateway.list(path);

        for (FileInfo f : files) {
            System.out.println(f.getRemoteDirectory()+f.getFilename());
        }


    }

    public void delete(String path) {
        Boolean isDelete = ftpGateway.delete(path);

        if (isDelete) {
            System.out.println("Deleted");
        } else {
            System.out.println("Error");
        }
    }

    public void get(String filePath) {

        InputStream inputStream = ftpGateway.get(filePath);

        try {
            System.out.println(inputStream.read());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
