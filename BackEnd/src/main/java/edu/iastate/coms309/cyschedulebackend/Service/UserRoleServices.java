package edu.iastate.coms309.cyschedulebackend.Service;
import edu.iastate.coms309.cyschedulebackend.persistence.model.Permission;
import edu.iastate.coms309.cyschedulebackend.persistence.model.UserRole;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;

@Service
public class UserRoleServices{

    SessionFactory sessionFactory;

    @Autowired
    public UserRoleServices(EntityManagerFactory factory) {
        if(factory.unwrap(SessionFactory.class) == null){
            throw new NullPointerException("factory is not a hibernate factory");
        }
        this.sessionFactory = factory.unwrap(SessionFactory.class);
    }

    public UserRole addNewUserRole(String name, String description) {
        Session session = sessionFactory.openSession();

        session.beginTransaction();

        UserRole userRole = new UserRole();

        userRole.setRoleName(name);

        userRole.setDescription(description);

        //update Redis Cached Value
        session.save(userRole);

        session.getTransaction().commit();

        session.close();

        return userRole;
    }

    public void addPermissionToRole(UserRole userRole, Permission permission) {
        Session session=sessionFactory.openSession();

        userRole.getRolePermissions().add(permission);

        session.beginTransaction();

        session.update(userRole);

        session.getTransaction().commit();

        session.close();
    }
}
