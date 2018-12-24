package com.example.ftp;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import org.junit.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.*;

public class HashingTest {

    @Test
    public void test() {

        File f = new File("/data/tes1t.rtf");

        try {
            FileInputStream fileInputStream = new FileInputStream(f);
            MockMultipartFile mockMultipartFile = new MockMultipartFile(f.getName(), fileInputStream);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write(mockMultipartFile.getBytes());
            byteArrayOutputStream.write(mockMultipartFile.getName().getBytes());

            String test = Hashing.sha256().hashBytes(byteArrayOutputStream.toByteArray()).toString();

            System.out.println(test + "." + Files.getFileExtension(mockMultipartFile.getName()));

            System.out.println(byteArrayOutputStream.size());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
