package mpa;

import static org.mockito.Mockito.mock;
import mpa.mad.MadHome;
import mpa.mad.MadHomeImpl;
import mpa.pair.PairHome;
import mpa.pair.PairHomeImpl;
import mpa.scenary.ScenaryHomeImpl;
import mpa.search.ScenaryBuilder;
import mpa.search.ScenaryEvaluator;

import org.picocontainer.DefaultPicoContainer;
import org.picocontainer.PicoContainer;
import org.picocontainer.behaviors.Caching;

public class ContainerFactory {

    public static PicoContainer createContainer() {
        DefaultPicoContainer container = new DefaultPicoContainer(new Caching());
        container.addComponent(ScenaryHomeImpl.class);
        container.addComponent(PairHome.class);
        container.addComponent(MadHome.class);
        return container;
    }
    
    public static PicoContainer createMockContainer(){
        DefaultPicoContainer container = new DefaultPicoContainer(new Caching());
        container.addComponent(mock(ScenaryHomeImpl.class));
        container.addComponent(mock(PairHomeImpl.class));
        container.addComponent(mock(MadHomeImpl.class));
        container.addComponent(mock(ScenaryBuilder.class));
        container.addComponent(mock(ScenaryEvaluator.class));
        return container;        
    }

}
