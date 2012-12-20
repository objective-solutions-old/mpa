package mpa;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import mpa.mad.Mad;
import mpa.mad.MadHome;
import mpa.search.ScenarySearch;
import mpa.search.exhaustive.ExhaustiveSearch;

import org.junit.Before;
import org.junit.Test;
import org.picocontainer.DefaultPicoContainer;

public class ExhaustiveSearchTest {

    private DefaultPicoContainer container = null;//new DefaultPicoContainer(ContainerFactory.createMockContainer());

    // private ScenaryEvaluator evaluator =
    // container.getComponent(ScenaryEvaluator.class);
    private MadHome madHome = container.getComponent(MadHome.class);;
    private ScenarySearch searchImpl;

    @Before
    public void setUp() {
        container.addComponent(ExhaustiveSearch.class);
        searchImpl = container.getComponent(ExhaustiveSearch.class);
    }

    @Test
    public void test() {
        searchImpl.search();
    }

    public void setupMadHomeMock() {
        ArrayList<Mad> mads = new ArrayList<Mad>();
        mads.add(createMadMock("A"));
        mads.add(createMadMock("B"));
        mads.add(createMadMock("C"));
        mads.add(createMadMock("D"));
        when(madHome.activeMads()).thenReturn(mads);
    }

    public Mad createMadMock(final String name) {
        Mad mock = mock(Mad.class);
        when(mock.toString()).thenReturn(name);
        return mock;
    }
}
