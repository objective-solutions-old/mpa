package mpa.pair;

import mpa.mad.Mad;


public class PairHomeImpl implements PairHome{

	private static final long serialVersionUID = 7704467646957641764L;
	
	@Override
	public PairImpl createPair(Mad mad1, Mad mad2) {
	    return new PairImpl(mad1, mad2);
	}
}
