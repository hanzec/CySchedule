package edu.iastate.coms309.cyschedulebackend.Service.file;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import edu.iastate.coms309.cyschedulebackend.exception.io.FileUploadFailedException;
import edu.iastate.coms309.cyschedulebackend.persistence.dao.FileManagementService;
import edu.iastate.coms309.cyschedulebackend.persistence.model.FileObject;
import edu.iastate.coms309.cyschedulebackend.persistence.repository.FileObjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class S3FileManagementService extends FileManagementService {
    /*
       Maybe a improve point
           - need to handle the exception

    */

    @Value("${storage.bucketName}")
    private String bucketName;

    private AmazonS3 bucket = null;

    @Autowired
    FileObjectRepository fileObjectRepository;

    public S3FileManagementService(String region,String accessKey, String s3EndPoint, String accessSecret){
        AWSCredentialsProvider doCred = new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, accessSecret));
        bucket = AmazonS3ClientBuilder.standard()
                .withCredentials(doCred)
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(s3EndPoint, region))
                .build();
    }

    @Override
    public String getFile(FileObject fileObject) {
        java.util.Date expiration = new java.util.Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60;
        expiration.setTime(expTimeMillis);

        // Generate the presigned URL.
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName,fileObject.toString())
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);
        return bucket.generatePresignedUrl(generatePresignedUrlRequest).toString();
    }

    @Override
    public void deleteFile(FileObject fileObject) {
        bucket.deleteObject(bucketName,fileObject.toString());
        fileObjectRepository.delete(fileObject);
    }

    @Override
    public FileObject putFile(MultipartFile file,String fileType) throws FileUploadFailedException {
        //construct file object Metadata
        ObjectMetadata data = new ObjectMetadata();
        data.setContentLength(file.getSize());
        data.setContentType(file.getContentType());

        //save the file object to mysql
        FileObject fileObject = new FileObject();
        fileObject.setFileType(fileType);
        fileObject.setFileSize(file.getSize());
        fileObject.setFileName(file.getOriginalFilename());
        fileObject.setFileId(UUID.randomUUID().toString());

        //upload to server
        try {
            bucket.putObject(bucketName, fileObject.toString(),file.getInputStream(), data);
        } catch (IOException e) {
            throw new FileUploadFailedException();
        }

        fileObjectRepository.save(fileObject);

        return fileObject;
    }
}
