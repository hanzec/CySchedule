package edu.iastate.coms309.cyschedulebackend.persistence.dao;


import edu.iastate.coms309.cyschedulebackend.persistence.model.FileObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public interface FileManagementService {

    public String getFile(FileObject fileObject);

    public void deleteFile(FileObject fileObject);

    public FileObject putFile(MultipartFile file, String fileType) throws IOException;
}
