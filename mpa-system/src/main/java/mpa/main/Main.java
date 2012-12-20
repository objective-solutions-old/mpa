package mpa.main;

import mpa.main.formatter.ScenaryFormatter;
import mpa.main.formatter.SqlFormatter;
import mpa.scenary.Scenary;
import mpa.search.ScenarySearch;
import mpa.search.evaluator.PredefinedPairs;

import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import org.picocontainer.Startable;
import org.picocontainer.behaviors.Caching;

public class Main implements Startable {

    private static MutablePicoContainer container;
    private final ScenarySearch scenarySearch;
    private final ScenaryFormatter formatter;

    public Main(ScenarySearch scenarySearch, ScenaryFormatter formatter, SqlFormatter sqlFormatter, PredefinedPairs pred) {
    	this.scenarySearch = scenarySearch;
        this.formatter = formatter;
    }

    public static void main(String[] args) {
    	PicoContainer parentContainer = SystemContainerFactory.buildContainer();
    	container = new DefaultPicoContainer(new Caching(), parentContainer);
        container.addComponent(Main.class);
        ((Startable) parentContainer).start();
        container.start();
    }
    
    @Override
    public void start() {
        Scenary scenary = scenarySearch.search();
        System.out.println(formatter.format(scenary));
    }

    @Override
    public void stop() {
    }
}
