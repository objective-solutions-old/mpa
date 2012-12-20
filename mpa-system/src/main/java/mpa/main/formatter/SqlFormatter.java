package mpa.main.formatter;

import mpa.pair.Pair;
import mpa.scenary.Scenary;

public class SqlFormatter {

	private static final String SQL = "insert into mad.mesa values (null, (select max(id) from mad.mpa_configuracao)" +
			"\n     , %d" +
			"\n	    , (select id from mad.objectiviano where nome = '%s')" +
			"\n	    , (select id from mad.objectiviano where nome = '%s'));";

	public String format(Scenary scenary){
		int mesa = 1;
		StringBuilder builder = new StringBuilder();
		for(Pair pair: scenary.getPairs())
			builder.append(formatPair(mesa++, pair)).append("\n");
		return builder.toString();
	}

	private String formatPair(int mesa, Pair pair) {
		return String.format(SQL, mesa, pair.getMads().first(), pair.getMads().last());
	}
		
}
