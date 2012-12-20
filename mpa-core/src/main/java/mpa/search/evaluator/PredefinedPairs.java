package mpa.search.evaluator;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import mpa.mad.Mad;
import mpa.pair.Pair;

public class PredefinedPairs {

	private Map<Mad, Set<Mad>> expectedMap;
	private Map<Mad, Set<Mad>> rejectedMap;

	public PredefinedPairs() {
		expectedMap = new HashMap<Mad, Set<Mad>>();
		rejectedMap = new HashMap<Mad, Set<Mad>>();
	}
	
	public boolean validate(Collection<Pair> pairs) {
		for (Pair p : pairs) {
			Mad first = p.getMads().first();
			Mad last = p.getMads().last();
			if(!test(first, last))
				return false;
			if(!test(last, first))
				return false;
		}
		return true;
	}

	private boolean test(Mad first, Mad last) {
		if(rejectedMap.containsKey(first) && rejectedMap.get(first).contains(last))
			return false;
		if(expectedMap.containsKey(first) && !expectedMap.get(first).contains(last))
			return false;
		return true;
	}

	public boolean isPredefined(Pair pair) {
		return expectedMap.containsKey(pair.getMads().first()) && isExpected(pair);
	}
	
	private boolean isExpected(Pair pair) {
		Mad first = pair.getMads().first();
		Mad last = pair.getMads().last();
		return isExpected(first, last);
	}

	private boolean isExpected(Mad first, Mad last) {
		return testForMap(expectedMap, first, last);
	}

	private boolean testForMap(Map<Mad, Set<Mad>> map, Mad first, Mad last) {
		if(!map.containsKey(first))
			return false;
		return map.get(first).contains(last);
	}

	public void expectPair(Pair pair) {
		putPair(expectedMap, pair);
	}

	public void rejectPair(Pair pair) {
		putPair(rejectedMap, pair);
	}

	private void putPair(Map<Mad, Set<Mad>> map, Pair pair) {		
		add(map, pair.getMads().first(), pair.getMads().last());
		add(map, pair.getMads().last(), pair.getMads().first());
	}
	
	private void add(Map<Mad, Set<Mad>> map, Mad mad1, Mad mad2){
		if(!map.containsKey(mad1))
			map.put(mad1, new HashSet<Mad>());
		map.get(mad1).add(mad2);
	}


}
