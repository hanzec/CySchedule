package edu.iastate.coms309.cyschedulebackend.Service;

import edu.iastate.coms309.cyschedulebackend.persistence.model.Permission;
import edu.iastate.coms309.cyschedulebackend.persistence.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


public class PermissionService {

    @Autowired
    PermissionRepository permissionRepository;

    public Permission loadPermissionById(Integer permissionID){
        return permissionRepository.findByRoleID(permissionID);
    }

    public Boolean isPermissionExist(Integer permissionID){
        return permissionRepository.existsByRoleID(permissionID);
    }
}
