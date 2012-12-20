package mpa.search.exhaustive;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Combination<T extends Comparable<T>> implements Iterable<Dupla<T>> {

	private final List<Dupla<T>> conteudo;

	public Combination(Dupla<T> element){
		this();
		conteudo.add(element);
	}
	
	public Combination() {
		this(new ArrayList<Dupla<T>>());
	}
	
	public Combination(List<Dupla<T>> duplas) {
		conteudo = duplas;
	}

	public boolean add(Dupla<T> e) {
		return conteudo.add(e);
	}
	
	public Combination<T> merge(Combination<T> combination) {
		List<Dupla<T>> l = new ArrayList<Dupla<T>>(conteudo.size() + combination.conteudo.size());
		l.addAll(conteudo);
		l.addAll(combination.conteudo);
		return new Combination<T>(l);
	}

	
	@Override
	public String toString() {
		return conteudo.toString();
	}

	public Iterator<Dupla<T>> iterator() {
		return conteudo.iterator();
	}
	
	public boolean isEmpty(){
	    return conteudo.isEmpty();
	}

}
