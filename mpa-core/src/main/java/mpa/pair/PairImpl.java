package mpa.pair;

import java.util.NavigableSet;
import java.util.TreeSet;

import mpa.mad.Mad;


public class PairImpl implements Pair {

	private static final long serialVersionUID = 2496783389419108395L;

	private final TreeSet<Mad> mads = new TreeSet<Mad>();

	public PairImpl(Mad mad1, Mad mad2){
		mads.add(mad1);
        mads.add(mad2);
	}
	
	@Override
	public int compareTo(Pair o) {
		int c = mads.first().compareTo(o.getMads().first());
		return c != 0? c: mads.last().compareTo(o.getMads().last());
	}

	public boolean containsMad(Mad mad) {
		return mads.contains(mad);
	}

	@Override
	public String toString() {
		return mads.first() + " / " + mads.last();
	}
    
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof PairImpl))
            return false;
        PairImpl other = (PairImpl) obj;
        return this.mads.equals(other.mads); 
    }

    @Override
    public NavigableSet<Mad> getMads() {
        return mads;
    }

//    public void swapPairs(Pair other) {
//        Mad thisLast = this.mads.pollLast();
//        Mad otherLast = other.mads.pollLast();
//        this.mads.add(otherLast);
//        other.mads.add(thisLast);
//    }
}
