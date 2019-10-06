package edu.iastate.coms309.cyschedulebackend.Service;

import edu.iastate.coms309.cyschedulebackend.persistence.dao.PermissionDao;
import edu.iastate.coms309.cyschedulebackend.persistence.model.Permission;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManagerFactory;

public class PermissionService{

    SessionFactory sessionFactory;

    @Autowired
    public PermissionService(EntityManagerFactory factory) {
        if(factory.unwrap(SessionFactory.class) == null){
            throw new NullPointerException("factory is not a hibernate factory");
        }
        this.sessionFactory = factory.unwrap(SessionFactory.class);
    }

    public void deletePermission(Permission permission) {
        Session session = sessionFactory.openSession();

        session.delete(session.get(Permission.class,permission.getPermissionID()));

        session.close();
    }

    public Permission addPermission(String name, String description) {
        Session session = sessionFactory.openSession();

        session.beginTransaction();

        Permission permission = new Permission();

        permission.setPermissionName(name);

        permission.setPermissionDescription(description);

        //update Redis Cached Value
        session.save(permission);

        session.getTransaction().commit();

        session.close();

        return permission;
    }
}
