package mpa.scenary;

import mpa.pair.Pair;

public class LastOcurrenceNotFound extends RuntimeException {

    private static final long serialVersionUID = 2088205749058307236L;

    public LastOcurrenceNotFound(Pair p) {
        super(String.valueOf(p));
    }
}
