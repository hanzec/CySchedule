package edu.iastate.coms309.cyschedulebackend.persistence.repository;

import edu.iastate.coms309.cyschedulebackend.persistence.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long>{
}
