package edu.iastate.coms309.cyschedulebackend.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import edu.iastate.coms309.cyschedulebackend.persistence.model.FileObject;
import org.springframework.stereotype.Repository;

@Repository
public interface FileObjectRepository extends JpaRepository<FileObject,String> {
}
