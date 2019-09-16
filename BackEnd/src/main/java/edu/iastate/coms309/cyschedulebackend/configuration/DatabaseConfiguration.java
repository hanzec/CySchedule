package edu.iastate.coms309.cyschedulebackend.configuration;

import org.hibernate.SessionFactory;
import javax.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;

@Configuration
public class DatabaseConfiguration {

    @Bean
    public SessionFactory sessionFactory(@Qualifier("entityManagerFactory") EntityManagerFactory emf){
        return emf.unwrap(SessionFactory.class);
    }
}
