package edu.iastate.coms309.cyschedulebackend.persistence.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TimeBlock implements Serializable{

    @Id
    @Column(name = "tbid", nullable = false)
    private User master;

    private Date start;

    private Date end;

    private String act;

    private int type;

    private boolean regular;

    private String instruction;

    public TimeBlock(User master, String act, int type, Date start, Date end, boolean regular, String instruction){

        this.master = master;
        this.act = act;
        this.type = type;
        this.start = start;
        this.end = end;
        this.regular = regular;
        this.instruction = instruction;
    }

    public void totaledit(String act, int type, Date start, Date end, boolean regular, String instruction){
        this.act = act;
        this.type = type;
        this.start = start;
        this.end = end;
        this.regular = regular;
        this.instruction = instruction;
    }

    public void editinstruction(String instruction){
        this.instruction = instruction;
    }

    public void editTime(Date start, Date end){
        this.start = start;
        this.end = end;
    }

    public void editName(String act){
        this.act = act;
    }



    public String getAct(){
        return this.act;
    }

    public int getType(){
        return type;
    }

    public Date getStart(){
        return start;
    }

    public Date getEnd(){
        return end;
    }

    public boolean isRegular(){
        return regular;
    }

    public String getInstruction(){
        return instruction;
    }
}
