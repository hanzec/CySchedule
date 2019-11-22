package edu.iastate.coms309.cyschedulebackend.exception.io;

import javax.servlet.http.PushBuilder;

public class FileUploadFailedException extends Exception {
    public FileUploadFailedException(){super("file upload failed");}
}
