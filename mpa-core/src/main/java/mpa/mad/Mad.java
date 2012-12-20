package mpa.mad;

import java.util.Date;

import mpa.entity.Entity;

public interface Mad extends Entity<Mad>{

    String getName();

    Date getInitialDate();

    void setActive(Boolean isActive);
    
    boolean isNewbie(Date date);

    boolean isActive();    
}
