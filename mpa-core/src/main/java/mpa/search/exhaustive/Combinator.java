package mpa.search.exhaustive;

import java.util.ArrayList;
import java.util.List;

public class Combinator<T extends Comparable<T>> {

	public List<Combination<T>> combine(List<T> valores) {
		ArrayList<Combination<T>> combinations = new ArrayList<Combination<T>>();
		
		if(valores.isEmpty()) {
			combinations.add(new Combination<T>());
			return combinations;
		}
		
		if (valores.size() % 2 != 0)
			throw new IllegalArgumentException(String.valueOf(valores));
		
		for (int i = 1; i < valores.size(); i++) {
			Dupla<T> dupla = new Dupla<T>(valores.get(0), valores.get(i));
			
			ArrayList<T> subValores = new ArrayList<T>();
			for (T valor : valores)
				if (!dupla.contains(valor))
					subValores.add(valor);
			
			Combination<T> x = new Combination<T>();
			
			x.add(dupla);
			
			for(Combination<T> c : combine(subValores)) {
				Combination<T> merge = x.merge(c);
				combinations.add(merge);
			}
		}
		
		return combinations;
	}

}
