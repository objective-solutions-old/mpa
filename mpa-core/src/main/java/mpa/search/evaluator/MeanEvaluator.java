package mpa.search.evaluator;

import java.util.Collection;

import mpa.pair.Pair;
import mpa.scenary.LastOcurrenceCache;
import mpa.scenary.LastOcurrenceNotFound;
import mpa.search.ScenaryEvaluator;

public class MeanEvaluator implements ScenaryEvaluator {

	private final LastOcurrenceCache lastOcurrenceCache;

	public MeanEvaluator(LastOcurrenceCache lastOcurrenceCache) {
		this.lastOcurrenceCache = lastOcurrenceCache;
	}

	@Override
	public int compare(Collection<Pair> o1, Collection<Pair> o2) {
		return 0;
	}

	@Override
	public int calculateScenaryScore(Collection<Pair> pairs) {
		if(pairs.isEmpty())
			return 0;
		
		int i = 0;
		for (Pair p : pairs)
			try {
				i += Integer.parseInt(lastOcurrenceCache.lastOccurrence(p).getKey());
			} catch (LastOcurrenceNotFound e) {
			}

		return 100 - (i / pairs.size());
	}
}
