package edu.iastate.coms309.cyschedulebackend.Service;
import edu.iastate.coms309.cyschedulebackend.persistence.model.Permission;
import edu.iastate.coms309.cyschedulebackend.persistence.repository.PermissionRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;

@Service
public class UserRoleServices{

   @Autowired
   PermissionRepository permissionRepository;

}
