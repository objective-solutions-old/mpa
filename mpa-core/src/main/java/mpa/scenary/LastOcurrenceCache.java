package mpa.scenary;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import mpa.pair.Pair;

public class LastOcurrenceCache {

    private final ScenaryHome scenaryHome;
    private Map<Pair, Scenary> lastOcurrenceCache = new HashMap<Pair, Scenary>();
    private Set<Pair> lastOcurrenceNotFoundCache = new HashSet<Pair>();

    public LastOcurrenceCache(ScenaryHome scenaryHome){
        this.scenaryHome = scenaryHome;
    }
    
    public Scenary lastOccurrence(Pair p) {
        if(lastOcurrenceCache.containsKey(p))
            return lastOcurrenceCache.get(p);
        
        if(lastOcurrenceNotFoundCache .contains(p))
            throw new LastOcurrenceNotFound(p);
        
        return calculateLastOcurrence(p);
    }

    private Scenary calculateLastOcurrence(Pair p) {
        try{
            Scenary lastOccurrence = scenaryHome.lastOccurrence(p);
            lastOcurrenceCache.put(p, lastOccurrence);
            return lastOccurrence;
        }catch(LastOcurrenceNotFound e){
            lastOcurrenceNotFoundCache.add(p);
            throw e;
        }
    }

}
