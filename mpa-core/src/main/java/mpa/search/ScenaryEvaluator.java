package mpa.search;

import java.util.Collection;
import java.util.Comparator;

import mpa.pair.Pair;

public interface ScenaryEvaluator extends Comparator<Collection<Pair>> {
    
    int calculateScenaryScore(Collection<Pair> pairs);
}
