package edu.iastate.coms309.cyschedulebackend.configuration;

import edu.iastate.coms309.cyschedulebackend.Service.file.S3FileManagementService;
import edu.iastate.coms309.cyschedulebackend.persistence.dao.FileManagementService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
public class FileStorageConfiguration {

    @Value("${storage.service}")
    String userService;

    @Value("${storage.region}")
    private String region;

    @Value("${storage.accessKey}")
    private String accessKey;

    @Value("${storage.s3EndPoint}")
    private String s3EndPoint;

    @Value("${storage.accessSecret}")
    private String accessSecret;

    @Bean
    public FileManagementService fileManagementService(){
        if(userService.equals("s3".toLowerCase()))
            return new S3FileManagementService(region,accessKey,s3EndPoint,accessSecret);
        else return null;
    }

}
