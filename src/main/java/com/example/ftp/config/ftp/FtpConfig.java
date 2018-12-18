package com.example.ftp.config.ftp;

import com.example.ftp.payload.UploadFile;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.StandardIntegrationFlow;
import org.springframework.integration.dsl.ftp.Ftp;
import org.springframework.integration.file.FileHeaders;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.ftp.inbound.FtpInboundFileSynchronizer;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

@Configuration
@IntegrationComponentScan
@EnableConfigurationProperties(FtpProperties.class)
public class FtpConfig {

    private static String REMOTE_DIR = "/";
    private final FtpProperties ftpProperties;

    public FtpConfig(FtpProperties ftpProperties) {
        this.ftpProperties = ftpProperties;
    }

    @Bean
    DefaultFtpSessionFactory ftpSessionFactory() {
        DefaultFtpSessionFactory ftpSessionFactory = new DefaultFtpSessionFactory();
        ftpSessionFactory.setHost(ftpProperties.getHost());
        ftpSessionFactory.setUsername(ftpProperties.getUsername());
        ftpSessionFactory.setPassword(ftpProperties.getPassword());
        return ftpSessionFactory;
    }

    @Bean
    public StandardIntegrationFlow ftpOutboundFlow(final DefaultFtpSessionFactory ftpSessionFactory) {
        return IntegrationFlows.from("ftpChannel")
                .transform(UploadFile.class, new GenericTransformer<UploadFile, Message>() {
                    @Override
                    public Message transform(UploadFile uploadFile) {
                        return MessageBuilder.withPayload(uploadFile.getPayload()).setHeader(FileHeaders.FILENAME, uploadFile.getName()).build();
                    }
                })
                .handle(Ftp.outboundAdapter(ftpSessionFactory, FileExistsMode.FAIL)
                        .useTemporaryFileName(true)
                        .remoteDirectory(REMOTE_DIR))
                .get();

    }


}
