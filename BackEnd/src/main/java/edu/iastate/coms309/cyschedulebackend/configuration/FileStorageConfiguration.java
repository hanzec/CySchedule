package edu.iastate.coms309.cyschedulebackend.configuration;

import edu.iastate.coms309.cyschedulebackend.Service.file.S3FileManagementService;
import edu.iastate.coms309.cyschedulebackend.persistence.dao.FileManagementService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FileStorageConfiguration {

    @Value("${storage.avatar.service}")
    String userService;

    @Bean
    public FileManagementService fileManagementService(){
        if(userService.equals("s3".toLowerCase()))
            return new S3FileManagementService();
        else return null;
    }

}
