package mpa.search.evaluator;

import java.util.Collection;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.TreeSet;

import mpa.pair.Pair;
import mpa.scenary.LastOcurrenceCache;
import mpa.scenary.LastOcurrenceNotFound;
import mpa.scenary.Scenary;
import mpa.scenary.ScenaryHome;
import mpa.search.ScenaryEvaluator;

public class MostRecentPairEvaluator implements ScenaryEvaluator {

	private final ScenaryHome scenaryHome;
	private final PredefinedPairs predefinedPairs;
    private LastOcurrenceCache lastOcurrenceCache;

	public MostRecentPairEvaluator(ScenaryHome scenaryHome, LastOcurrenceCache lastOcurrenceCache, PredefinedPairs predefinedPairs) {
		this.scenaryHome = scenaryHome;
        this.lastOcurrenceCache = lastOcurrenceCache;
		this.predefinedPairs = predefinedPairs;
	}

	@Override
	public int calculateScenaryScore(Collection<Pair> pairs) {
		if (pairs.isEmpty())
			return 0;
		if (scenaryHome.count() <= 1)
			return 100;
		return calculate(fillSet(pairs));
	}

	private NavigableSet<Scenary> fillSet(Collection<Pair> scenary) {
		TreeSet<Scenary> scenaries = new TreeSet<Scenary>();
		for (Pair p : scenary)
			try {
				if(!predefinedPairs.isPredefined(p))
					scenaries.add(lastOcurrenceCache.lastOccurrence(p));
			} catch (LastOcurrenceNotFound e) {
			}
		return scenaries;
	}

	private int calculate(NavigableSet<Scenary> scenaries) {
		try {
			float lastOcurrenceOrder = lastOcurrence(scenaries);
			return (int) (100 * (1 - lastOcurrenceOrder / (scenaryHome.count() - 1)));
		} catch (NoSuchElementException e) {
			return 100;
		}
	}

	private int lastOcurrence(NavigableSet<Scenary> scenaries) {
		return Integer.parseInt(scenaries.last().getKey());
	}

	@Override
	public int compare(Collection<Pair> o1, Collection<Pair> o2) {
		return (int) Math.signum(calculateScenaryScore(o1) - calculateScenaryScore(o2));
	}
}
