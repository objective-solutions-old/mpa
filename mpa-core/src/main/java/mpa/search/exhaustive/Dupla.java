package mpa.search.exhaustive;

import java.util.NavigableSet;
import java.util.TreeSet;

public class Dupla<T extends Comparable<T>> {
	private NavigableSet<T> dupla;

	public Dupla(T a, T b) {
		dupla = new TreeSet<T>();
		dupla.add(a);
		dupla.add(b);
	}
	
	public boolean contains(T t){
		return dupla.contains(t);
	}
	
	@Override
	public String toString() {
		return dupla.toString();
	} 

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Dupla<?> other = (Dupla<?>) obj;
		if (dupla == null) {
			if (other.dupla != null)
				return false;
		} else if (!dupla.equals(other.dupla))
			return false;
		return true;
	}

	public T last() {
		return dupla.last();
	}
	
	public T first(){
		return dupla.first();
	}
	
}
