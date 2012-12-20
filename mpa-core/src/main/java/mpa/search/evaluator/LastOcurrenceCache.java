package mpa.search.evaluator;

import java.util.HashMap;
import java.util.Map;

import mpa.pair.Pair;
import mpa.scenary.Scenary;
import mpa.scenary.ScenaryHome;

public class LastOcurrenceCache {

    private final ScenaryHome scenaryHome;
    private Map<Pair, Scenary> cache = new HashMap<Pair, Scenary>();;

    public LastOcurrenceCache(ScenaryHome scenaryHome){
        this.scenaryHome = scenaryHome;        
    }
    
    public Scenary lastOccurrence(Pair pair){
        if(!cache.containsKey(pair))
            cache.put(pair, scenaryHome.lastOccurrence(pair));
        return cache.get(pair);
    }
}
