package mpa.main;

import mpa.mad.MadHomeImpl;
import mpa.main.formatter.ScenaryFormatter;
import mpa.main.formatter.SqlFormatter;
import mpa.main.loader.ConfigFiles;
import mpa.main.loader.ExpectedLoaderFile;
import mpa.main.loader.Loader;
import mpa.main.loader.MpaLoader;
import mpa.main.loader.TeamLoaderFile;
import mpa.pair.PairHomeImpl;
import mpa.scenary.LastOcurrenceCache;
import mpa.scenary.ScenaryHome;
import mpa.scenary.ScenaryHomeImpl;
import mpa.search.evaluator.CompoundScenaryEvaluator;
import mpa.search.evaluator.MeanEvaluator;
import mpa.search.evaluator.MostRecentPairEvaluator;
import mpa.search.evaluator.PredefinedPairs;
import mpa.search.exhaustive.ExhaustiveSearch;

import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.PicoContainer;
import org.picocontainer.behaviors.Caching;

public class SystemContainerFactory {
    
    public static PicoContainer buildContainer() {
        DefaultPicoContainer container = new DefaultPicoContainer(new Caching());

        container.addComponent(MadHomeImpl.class);
        container.addComponent(PairHomeImpl.class);
        container.addComponent(LastOcurrenceCache.class);
        container.addComponent(ScenaryHome.class, ScenaryHomeImpl.class);
        container.addComponent(ConfigFiles.class);
        
        container.addComponent(TeamLoaderFile.class);
        container.addComponent(MpaLoader.class);
        container.addComponent(ExpectedLoaderFile.class);
        container.addComponent(Loader.class);

        container.addComponent(PredefinedPairs.class);
        container.addComponent(MostRecentPairEvaluator.class);
        container.addComponent(MeanEvaluator.class);   
        container.addComponent(CompoundScenaryEvaluator.class);
        
        container.addComponent(ScenaryFormatter.class);
        container.addComponent(SqlFormatter.class);
        
        container.addComponent(ExhaustiveSearch.class);        
        return container;
    }
    
    public static PicoContainer buildBasicContainer() {
        DefaultPicoContainer container = new DefaultPicoContainer(new Caching());
        
        container.addComponent(MadHomeImpl.class);
        container.addComponent(PairHomeImpl.class);
        container.addComponent(LastOcurrenceCache.class);
        container.addComponent(ScenaryHome.class, ScenaryHomeImpl.class);
        
        container.addComponent(PredefinedPairs.class);
        container.addComponent(MostRecentPairEvaluator.class);
        container.addComponent(MeanEvaluator.class);   
        container.addComponent(CompoundScenaryEvaluator.class);
        
        container.addComponent(ExhaustiveSearch.class);        
        return container;
    }


}
