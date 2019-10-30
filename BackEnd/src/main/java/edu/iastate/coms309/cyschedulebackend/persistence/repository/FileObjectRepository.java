package edu.iastate.coms309.cyschedulebackend.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import edu.iastate.coms309.cyschedulebackend.persistence.model.FileObject;

public interface FileObjectRepository extends JpaRepository<FileObject,String> {
}
