package mpa.entity;

import java.io.Serializable;

public interface Entity<T> extends Serializable, Comparable<T>{ 
	String getKey();
	int compareTo(T o);
}
