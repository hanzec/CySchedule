package edu.iastate.coms309.cyschedulebackend.persistence.model;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class FileObject{

    @Id
    private String fileId;

    private long fileSize;

    private String fileType;

    private String fileName;

    @Override
    public String toString(){
        return fileType + "/" + fileName;
    }
}
