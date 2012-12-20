package mpa.pair;

import java.io.Serializable;
import java.util.NavigableSet;

import mpa.mad.Mad;

public interface Pair extends Serializable, Comparable<Pair>{

    NavigableSet<Mad> getMads();

    boolean containsMad(Mad mad);
}
