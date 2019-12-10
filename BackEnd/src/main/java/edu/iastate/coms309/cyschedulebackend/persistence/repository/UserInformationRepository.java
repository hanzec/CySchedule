package edu.iastate.coms309.cyschedulebackend.persistence.repository;

import edu.iastate.coms309.cyschedulebackend.persistence.model.FileObject;
import edu.iastate.coms309.cyschedulebackend.persistence.model.UserInformation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserInformationRepository extends JpaRepository<UserInformation, String> {

//    @Query("SELECT UserInformation.avatar FROM UserInformation u  WHERE a.userID = ?1")
//    FileObject getUserAvatar(String id);

//    @Query("SELECT UserInformation FROM UserInformation u  WHERE a.userID = ?1 AND UserInformation.avatar IS NULL ")
//    boolean isAvatarExist(String id);
}