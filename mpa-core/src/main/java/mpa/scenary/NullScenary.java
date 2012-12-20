package mpa.scenary;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import mpa.mad.Mad;
import mpa.pair.Pair;
import mpa.pair.PairImpl;


@SuppressWarnings("serial")
public class NullScenary implements Scenary {

    @Override
    public String getKey() {
        return "0";
    }

    @Override
    public int compareTo(Scenary o) {
        return -1;
    }

    @Override
    public List<Pair> getPairs() {
        return Collections.emptyList();
    }

    @Override
    public boolean containsPair(Pair p) {
        return false;
    }

    @Override
    public boolean hasPairs() {
        return false;
    }

    @Override
    public PairImpl pairForMad(Mad mad) {
        throw new PairNotFound();
    }

	@Override
	public Date getStartDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(0);
		return calendar.getTime();
	}

	@Override
	public void addPair(Pair createPair) {
	}
}
