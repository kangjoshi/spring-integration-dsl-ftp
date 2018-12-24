package com.example.ftp.config.ftp;

import com.example.ftp.payload.UploadFile;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.*;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.StandardIntegrationFlow;
import org.springframework.integration.dsl.ftp.Ftp;
import org.springframework.integration.dsl.ftp.FtpMessageHandlerSpec;
import org.springframework.integration.dsl.ftp.FtpOutboundGatewaySpec;
import org.springframework.integration.file.FileHeaders;
import org.springframework.integration.file.remote.gateway.AbstractRemoteFileOutboundGateway;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.messaging.*;
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
    public DefaultFtpSessionFactory ftpSessionFactory() {
        DefaultFtpSessionFactory ftpSessionFactory = new DefaultFtpSessionFactory();
        ftpSessionFactory.setHost(ftpProperties.getHost());
        ftpSessionFactory.setUsername(ftpProperties.getUsername());
        ftpSessionFactory.setPassword(ftpProperties.getPassword());
        return ftpSessionFactory;
    }

    @Bean
    public MessageChannel outChannel() {
        return new QueueChannel();
    }

    @Bean
    public FtpMessageHandlerSpec ftpUploadHandler(final DefaultFtpSessionFactory ftpSessionFactory) {
        return Ftp.outboundAdapter(ftpSessionFactory, FileExistsMode.REPLACE)
                .useTemporaryFileName(true)
                .autoCreateDirectory(true)
                .fileNameExpression("headers['" + FileHeaders.FILENAME + "']")
                .remoteDirectory(REMOTE_DIR);
    }

    @Bean
    public StandardIntegrationFlow ftpUploadFlow(final FtpMessageHandlerSpec ftpUploadHandler) {
        return IntegrationFlows.from("ftpUpload")
                .transform(UploadFile.class, new GenericTransformer<UploadFile, Object>() {
                    @Override
                    public Message transform(UploadFile uploadFile) {
                        return MessageBuilder.withPayload(uploadFile.getPayload()).setHeader(FileHeaders.FILENAME, uploadFile.getName()).build();
                    }
                })
                .handle(ftpUploadHandler)
                .get();
    }

    @Bean
    public FtpOutboundGatewaySpec ftpDeleteHandler(final DefaultFtpSessionFactory ftpSessionFactory) {
        return Ftp.outboundGateway(ftpSessionFactory, AbstractRemoteFileOutboundGateway.Command.RM, "payload");
    }

    @Bean
    public IntegrationFlow ftpDeleteFlow(final @Qualifier("ftpDeleteHandler") FtpOutboundGatewaySpec ftpDeleteHandler) {
        return IntegrationFlows.from("ftpDelete")
                .handle(ftpDeleteHandler)
                .get();
    }

    @Bean
    public FtpOutboundGatewaySpec ftpListHandler(final DefaultFtpSessionFactory ftpSessionFactory) {
        return Ftp.outboundGateway(ftpSessionFactory, AbstractRemoteFileOutboundGateway.Command.LS, "payload")
                .options(AbstractRemoteFileOutboundGateway.Option.RECURSIVE);
    }

    @Bean
    public IntegrationFlow ftpListFlow(final @Qualifier("ftpListHandler") FtpOutboundGatewaySpec ftpListHandler) {
        return IntegrationFlows.from("ftpList")
                .handle(ftpListHandler)
                .get();
    }

    @Bean
    public FtpOutboundGatewaySpec ftpGetHandler(final DefaultFtpSessionFactory ftpSessionFactory) {
        return Ftp.outboundGateway(ftpSessionFactory, AbstractRemoteFileOutboundGateway.Command.GET, "payload")
                .options(AbstractRemoteFileOutboundGateway.Option.STREAM);
    }

    @Bean
    public IntegrationFlow ftpGetFlow(final @Qualifier("ftpGetHandler") FtpOutboundGatewaySpec ftpGetHandler) {
        return IntegrationFlows.from("ftpGet")
                .handle(ftpGetHandler)
                .get();
    }



}
