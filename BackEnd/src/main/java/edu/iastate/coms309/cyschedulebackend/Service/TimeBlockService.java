package edu.iastate.coms309.cyschedulebackend.Service;



import edu.iastate.coms309.cyschedulebackend.persistence.model.User;
import edu.iastate.coms309.cyschedulebackend.persistence.model.TimeBlock;
import edu.iastate.coms309.cyschedulebackend.persistence.dao.TimeBlockDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Date;

@Service
public class TimeBlockService implements  TimeBlockDAO{

    @Autowired
    SessionFactory sessionFactory;

    @Override
    TimeBlock addnewBlock(User user, String act, String instruction, int type, Date start, Date end, boolean regular){
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        TimeBlock tb = new TimeBlock(user,act,type,start,end,regular,instruction);

        session.save(tb);

        session.getTransaction().commit();

        session.close();

        return tb;
    }

    @Override
    public void deleteBlock(User user, String act){
        Session session = sessionFactory.openSession();

        session.delete(session.get(TimeBlock.class,user,act));

        session.close();
    }


    @Override
    public TimeBlock editBlock(String originText, String act, String instruction, int type, Date start, Date end, boolean regular)
    {
        Session session = sessionFactory.openSession();

        TimeBlock tb = session.get(TimeBlock.class,originText);
        tb.totaledit(act,type,start,end,regular,instruction);

        session.update(tb);
        session.close();
        return tb;
    }

    @Override
    public TimeBlock getCertainBlock(User user, String act){
        Session session = sessionFactory.openSession();

        TimeBlock tb = session.get(TimeBlock.class,user,act);

        session.close();
        return tb;
    }

    @Override
    public TimeBlock edittime(String act, Date start, Date end){
        Session session = sessionFactory.openSession();

        TimeBlock tb = session.get(TimeBlock.class,act);
        tb.editTime(start,end);
        session.update(tb);

        session.close();
        return tb;
    }

    @Override
    public TimeBlock editinst(String act, String instruction){
        Session session = sessionFactory.openSession();

        TimeBlock tb = session.get(TimeBlock.class,act);
        tb.editinstruction(instruction);
        session.update(tb);

        session.close();
        return tb;
    }

    @Override
    public TimeBlock[] PullAllRecord(User master){
        Session session = sessionFactory.openSession();

        TimeBlock TB[] = (TimeBlock) session.createQuery("from TimeBlock where TimeBlock.master = :master")
                .setParameter("master",master);

        return TB;
    }


}
