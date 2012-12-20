package mpa.mad;

import java.util.Calendar;
import java.util.Date;


public class MadImpl implements Mad {

    private static final long MILLISECONS_PER_DAY = 24 * 60 * 60 * 1000;
    private static final long NEWBIE_PERIOD_LENGTH = 120;
    private static final long serialVersionUID = -8534015181387144276L;

    private String name;
    private Date startDate;
    private Boolean active;

    public MadImpl(String name) {
        this(name, Calendar.getInstance().getTime());
    }

    public MadImpl(String name, Date startDate) {
        this.name = name;
        this.startDate = startDate;
        active = true;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Date getInitialDate() {
        return startDate;
    }

    @Override
    public String getKey() {
        return getName();
    }

    @Override
    public int compareTo(Mad other) {
        return name.compareTo(other.getName());
    }

    @Override
    public boolean isNewbie(Date date) {
        return (now() - startDate.getTime()) * MILLISECONS_PER_DAY < NEWBIE_PERIOD_LENGTH;
    }

    private long now() {
        return Calendar.getInstance().getTime().getTime();
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(Boolean isActive) {
        this.active = isActive;
    }
    
    @Override
    public boolean equals(Object mad) {
        return (mad instanceof Mad) && getName().equals(((Mad) mad).getName());
    }
    
    @Override
    public String toString() {
        return getName();
    }
}
