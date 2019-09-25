package edu.iastate.coms309.cyschedulebackend.persistence.dao;

import edu.iastate.coms309.cyschedulebackend.persistence.model.TimeBlock;
import edu.iastate.coms309.cyschedulebackend.persistence.model.User;

import java.util.Date;

public interface TimeBlockDAO {

    TimeBlock addnewBlock(User user, String act, int type, Date start, Date end, boolean regular);

    void deleteBlock(User user, String act);

    TimeBlock editBlock(String originText, String act, int type, Date start, Date end, boolean regular);

    TimeBlock getCertainBlock(User user, String act);

    TimeBlock[] PullAllRecord(User user);

    TimeBlock edittime(User user, String act, Date start, Date end);

    TimeBlock editinst(User user, String act, String instruction);


}
