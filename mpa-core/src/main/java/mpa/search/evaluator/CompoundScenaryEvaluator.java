package mpa.search.evaluator;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import mpa.pair.Pair;
import mpa.search.ScenaryEvaluator;

public class CompoundScenaryEvaluator implements ScenaryEvaluator{

	private final List<ScenaryEvaluator> evaluators = new LinkedList<ScenaryEvaluator>();

	public CompoundScenaryEvaluator(ScenaryEvaluator... evaluators) {
	    for(ScenaryEvaluator evaluator: evaluators)
	        this.evaluators.add(evaluator);
	}
	
	@Override
	public int compare(Collection<Pair> o1, Collection<Pair> o2) {
		return (int) Math.signum(calculateScenaryScore(o1) - calculateScenaryScore(o2));
	}

	@Override
	public int calculateScenaryScore(Collection<Pair> scenary) {
		int total = 0;
		for(ScenaryEvaluator evaluator: evaluators)
			total += evaluator.calculateScenaryScore(scenary);		
		return (evaluators.size() > 0 ? total / evaluators.size(): 0);
	}
	
	public String detailedScore(Collection<Pair> pairs){
		StringBuilder builder = new StringBuilder();
		for(ScenaryEvaluator evaluator: evaluators)
			builder.append(evaluator.getClass().getName())
					.append(" ")
					.append(evaluator.calculateScenaryScore(pairs))
					.append("\n");
		
		builder.append(getClass().getName())
				.append(" ")
				.append(calculateScenaryScore(pairs))
				.append("\n");
		return builder.toString();
	}
	
}
