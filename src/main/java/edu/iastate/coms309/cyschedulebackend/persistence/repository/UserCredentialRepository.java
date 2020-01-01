package edu.iastate.coms309.cyschedulebackend.persistence.repository;

import edu.iastate.coms309.cyschedulebackend.persistence.model.permission.Permission;
import edu.iastate.coms309.cyschedulebackend.persistence.model.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCredentialRepository extends JpaRepository<UserCredential, String> {

    @Query("SELECT email FROM UserCredential a WHERE a.userID = ?1")
    String getUserEmailByUserID(String id);

    List<UserCredential> getUserCredentialByPermissionsContains(Permission permission);
}
