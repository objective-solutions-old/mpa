package objective.scenary;

import static junit.framework.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import mpa.mad.MadImpl;
import mpa.pair.Pair;
import mpa.pair.PairImpl;
import mpa.scenary.LastOcurrenceNotFound;
import mpa.scenary.Scenary;
import mpa.scenary.ScenaryHomeImpl;

import org.junit.Test;

public class ScenaryHomeTest {

    private ScenaryHomeImpl scenaryHome = new ScenaryHomeImpl();
    private PairImpl pair1 = createPair("1", "A", "B");
    private PairImpl pair2 = createPair("2", "C", "D");

    @Test(expected = LastOcurrenceNotFound.class)
    public void noScenaries() {
        scenaryHome.lastOccurrence(pair1);
    }

    @Test(expected = LastOcurrenceNotFound.class)
    public void oneEmptyScenary() {
        createScenaryWithPairs();
        assertEquals(0, scenaryHome.lastOccurrence(pair1));
    }
    
    @Test(expected = LastOcurrenceNotFound.class)
    public void oneScenaryTwoPairsOneFirstTimer() {
        createScenaryWithPairs(pair1);
        assertEquals("1", scenaryHome.lastOccurrence(pair1).getKey());
        scenaryHome.lastOccurrence(pair2);
    }

    @Test
    public void oneScenaryTwoPairs() {
        createScenaryWithPairs(pair1, pair2);        
        assertEquals("1", scenaryHome.lastOccurrence(pair1).getKey());
        assertEquals("1", scenaryHome.lastOccurrence(pair2).getKey());
    }

    private void createScenaryWithPairs(PairImpl... pairs) {
        List<Pair> pairList = new LinkedList<Pair>();
        for (PairImpl p : pairs)
            pairList.add(p);
        Scenary scenary = null; //scenaryHome.createScenaryWithPairs(pairList);
        scenaryHome.add(scenary);
    }
    
    private PairImpl createPair(String id, String mad1, String mad2) {
        return new PairImpl(new MadImpl(mad1), new MadImpl(mad2));
    }
}
