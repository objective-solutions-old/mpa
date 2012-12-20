package mpa.main.formatter;

import java.text.DateFormat;

import mpa.mad.MadHome;
import mpa.mad.Team;
import mpa.pair.Pair;
import mpa.scenary.LastOcurrenceNotFound;
import mpa.scenary.Scenary;
import mpa.scenary.ScenaryHome;
import mpa.search.evaluator.CompoundScenaryEvaluator;

public class ScenaryFormatter {

    private final CompoundScenaryEvaluator evaluator;
	private DateFormat dateFormat;
	private StringBuilder builder;
	private ScenaryHome scenaryHome;
	private MadHome madHome;
    private int pairCount;

    public ScenaryFormatter(CompoundScenaryEvaluator evaluator, ScenaryHome scenaryHome, MadHome madHome){
        this.evaluator = evaluator;
		this.scenaryHome = scenaryHome;
		this.madHome = madHome;
        dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
    }
    
    public String format(Scenary scenary){
    	builder = new StringBuilder();
    	printScenaryHeader(scenary);    	
    	printTeams(scenary);    	
    	return builder.toString();
    }

	private void printTeams(Scenary scenary) {
	    pairCount = 1;
		for(Team team: madHome.getTeams())
    		format(team, scenary);
	}

	private void printScenaryHeader(Scenary scenary) {
		builder.append("Score: \n")
    			.append(evaluator.detailedScore(scenary.getPairs()))
    			.append("\n");
    	
		builder.append("mpa ")
    			.append(scenary.getKey())
    			.append(" ")
    			.append(dateFormat.format(scenary.getStartDate()))
    			.append("\n");
	}

	private void format(Team team, Scenary scenary) {
	    
		builder.append(team.getName())
				.append("\n");
		
		for(Pair pair: scenary.getPairs())
			if(team.contains(pair.getMads()))
				builder.append(pairCount++).append(" - ")
						.append(pair)
//						.append(" ")
//						.append(last(pair))
						.append("\n");
		
		builder.append("\n");
	}

	@SuppressWarnings("unused")
    private String last(Pair pair) {
		try{
			return scenaryHome.lastOccurrence(pair).getKey();
		}catch(LastOcurrenceNotFound e){
			return "-1";
		}
	}
}
