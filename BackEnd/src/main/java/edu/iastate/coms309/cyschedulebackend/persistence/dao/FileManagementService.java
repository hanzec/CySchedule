package edu.iastate.coms309.cyschedulebackend.persistence.dao;


import edu.iastate.coms309.cyschedulebackend.Service.file.S3FileManagementService;
import edu.iastate.coms309.cyschedulebackend.persistence.model.FileObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public abstract class FileManagementService implements AbstractService{

    public abstract String getFile(FileObject fileObject);

    public abstract void deleteFile(FileObject fileObject);

    public abstract FileObject putFile(MultipartFile file, String fileType) throws IOException;
}
