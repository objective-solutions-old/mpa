package mpa.scenary;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import mpa.mad.Mad;
import mpa.pair.Pair;


public class ScenaryImpl implements Scenary {

    private static final long serialVersionUID = -4753526515919812497L;
    
    private final List<Pair> pairs;
    private final Scenary previousScenary;
    private String key;
    private Date startDate;
	
	public ScenaryImpl(String key, Date startDate, Scenary previousScenary) {
		if(startDate == null)
			throw new IllegalArgumentException("Date null for scenary " + key);
		
        this.key = key;
		this.startDate = startDate;
        this.pairs = new LinkedList<Pair>();
        this.previousScenary = previousScenary;		
	}
	
	@Override
	public Date getStartDate() {
		return startDate;
	}

	public Scenary getPreviousScenary() {
        return previousScenary;
    }
	
    @Override
    public String getKey() {
        return key;
    }
    
    @Override
    public List<Pair> getPairs() {
        return pairs;
    }

    @Override
    public int compareTo(Scenary o) {
        Scenary other = (Scenary) o;
        return Integer.parseInt(key) - Integer.parseInt(other.getKey());
    }

    public boolean containsPair(Pair p) {
        return pairs.contains(p);
    }

    @Override
    public boolean hasPairs() {
        return !pairs.isEmpty();
    }

    @Override
    public Pair pairForMad(Mad mad) {
        for(Pair pair: pairs)
            if(pair.containsMad(mad))
                return pair;        
        throw new PairNotFound();
    }

	@Override
	public void addPair(Pair pair) {
		pairs.add(pair);
	}

}
