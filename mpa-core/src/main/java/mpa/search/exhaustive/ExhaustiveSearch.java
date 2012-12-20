package mpa.search.exhaustive;

import static java.util.Collections.max;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import mpa.mad.Mad;
import mpa.mad.MadHome;
import mpa.mad.Team;
import mpa.pair.Pair;
import mpa.pair.PairHome;
import mpa.scenary.Scenary;
import mpa.scenary.ScenaryHome;
import mpa.search.ScenaryEvaluator;
import mpa.search.ScenarySearch;
import mpa.search.evaluator.CompoundScenaryEvaluator;
import mpa.search.evaluator.PredefinedPairs;

public class ExhaustiveSearch implements ScenarySearch {

	private final ScenaryEvaluator evaluator;
	private final MadHome madHome;
	private final ScenaryHome scenaryHome;
	private final PairHome pairHome;
	private PredefinedPairs predefinedPairs;

	public ExhaustiveSearch(MadHome madHome, CompoundScenaryEvaluator evaluator, PredefinedPairs predefinedPairs, ScenaryHome scenaryHome, PairHome pairHome) {
		this.madHome = madHome;
		this.evaluator = evaluator;
		this.predefinedPairs = predefinedPairs;
		this.scenaryHome = scenaryHome;
		this.pairHome = pairHome;
	}
	
    @Override
    public Scenary search() {
    	Scenary scenary = scenaryHome.createUnattachedScenary();
    	for(Team team: madHome.getTeams())
    		for(Pair p: createPairs(team.getMads()))
    			scenary.addPair(p);
    	return scenary;
    }

	private Collection<Pair> createPairs(List<Mad> mads) {
		List<Collection<Pair>> scenaries = new LinkedList<Collection<Pair>>();
    	for(Combination<Mad> c: new Combinator<Mad>().combine(mads)) {
			Collection<Pair> pairs = createPairs(c);
			if(predefinedPairs.validate(pairs))
				scenaries.add(pairs);
		}
    	
    	if(scenaries.size() == 0)
    		throw new RuntimeException("Não foi possível gerar duplas para a equipe: \n" + mads);
    	
    	Collection<Pair> pairs = max(scenaries, evaluator);
        if(evaluator.calculateScenaryScore(pairs) == 0)
        	throw new RuntimeException();
		return pairs;
	}

	private Collection<Pair> createPairs(Combination<Mad> c) {
		Collection<Pair> scenary = new ArrayList<Pair>();
		for(Dupla<Mad> d: c)
			scenary.add(pairHome.createPair(d.first(), d.last()));
		return scenary;
	}

}
