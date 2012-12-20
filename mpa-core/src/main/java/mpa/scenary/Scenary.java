package mpa.scenary;

import java.util.Date;
import java.util.List;

import mpa.entity.Entity;
import mpa.mad.Mad;
import mpa.pair.Pair;


public interface Scenary extends Entity<Scenary> {
	
	Date getStartDate();
	
    List<Pair> getPairs();

    Pair pairForMad(Mad mad);

    boolean containsPair(Pair p);

    boolean hasPairs();

	void addPair(Pair pair);
}
