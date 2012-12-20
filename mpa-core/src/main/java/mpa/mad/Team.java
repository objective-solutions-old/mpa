package mpa.mad;

import java.util.LinkedList;
import java.util.List;

public class Team {

	private List<Mad> mads;
	private String name;
	
	public Team(String name) {		
		this.name = name;
		mads = new LinkedList<Mad>();
	}

	public List<Mad> getMads() {
		return mads;
	}

	public void addMad(Mad mad) {
		mads.add(mad);
	}
	
	@Override
	public String toString() {
		return String.format("Team(%s) %s", name, mads);
	}

	public boolean contains(Iterable<Mad> mads) {
		for(Mad mad: mads)
			if(!this.mads.contains(mad))
				return false;
		return true;
	}

	public String getName() {
		return name;
	}

}
