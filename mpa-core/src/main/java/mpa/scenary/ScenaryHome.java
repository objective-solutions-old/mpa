package mpa.scenary;

import java.util.Date;

import mpa.entity.EntityHome;
import mpa.pair.Pair;


public interface ScenaryHome extends EntityHome<Scenary> {

    public Scenary lastOccurrence(Pair p);

    public Scenary createScenary(int number, Date startDate);

    public Scenary lastScenary();

	public Scenary createUnattachedScenary();
}