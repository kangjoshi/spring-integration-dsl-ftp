package com.example.ftp.payload;

public class UploadFile {
    String name;
    byte[] payload;

    public UploadFile(String name, byte[] payload) {
        this.name = name;
        this.payload = payload;
    }

    public String getName() {
        return name;
    }

    public byte[] getPayload() {
        return payload;
    }
}
