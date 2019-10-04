package edu.iastate.coms309.cyschedulebackend.persistence.dao;

import edu.iastate.coms309.cyschedulebackend.persistence.model.UserRole;
import edu.iastate.coms309.cyschedulebackend.persistence.model.Permission;

import java.util.List;

public interface UserRoleDAO {

    public UserRole addNewUserRole(String name, String description);

    public void addPermissionToRole(UserRole userRole, Permission permission);
}
