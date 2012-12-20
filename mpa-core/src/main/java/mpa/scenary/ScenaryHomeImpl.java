package mpa.scenary;

import java.util.Date;
import java.util.LinkedList;

import mpa.entity.AbstractEntityHome;
import mpa.pair.Pair;


public class ScenaryHomeImpl extends AbstractEntityHome<Scenary> implements ScenaryHome{

    private static final long serialVersionUID = 1L;
    
    public ScenaryHomeImpl(){
        add(new NullScenary());
    }
    
    @Override
	public Scenary createScenary(int number, Date startDate) {
    	return add(new ScenaryImpl(String.valueOf(number), startDate, lastScenary()));
	}
    
    @Override
    public String getLabel() {
        return "Scenary";
    }

    @Override
    public Scenary lastScenary(){
        return all().get(count() - 1);
    }

    @Override
    public Scenary lastOccurrence(Pair p) {
        LinkedList<Scenary> scenaryStack = new LinkedList<Scenary>(all());        
        while(!scenaryStack.isEmpty()) {
            Scenary currentScenary = scenaryStack.removeLast();
            if(currentScenary.containsPair(p))
                return currentScenary;
        }
        throw new LastOcurrenceNotFound(p);
    }

	@Override
	public Scenary createUnattachedScenary() {
		return new ScenaryImpl(nextId(), new Date(), lastScenary());
	}

	private String nextId() {
		return String.valueOf(Integer.parseInt(lastScenary().getKey()) + 1);
	}
}
