package edu.iastate.coms309.cyschedulebackend.persistence.dao;

import edu.iastate.coms309.cyschedulebackend.persistence.model.Permission;

public interface PermissionDao {

    public void deletePermission(Permission permission);

    public Permission addPermission(String name, String description);
}
