package mpa.pair;

import java.io.Serializable;

import mpa.mad.Mad;

public interface PairHome extends Serializable{

    Pair createPair(Mad mad1, Mad mad2);
}
