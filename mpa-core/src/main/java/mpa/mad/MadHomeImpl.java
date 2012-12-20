package mpa.mad;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import mpa.entity.AbstractEntityHome;

public class MadHomeImpl extends AbstractEntityHome<Mad> implements MadHome {

    private static final long serialVersionUID = -8067575524707316860L;
	private List<Team> teams = new LinkedList<Team>();
	
    @Override
    public Mad createMad(String name, Date date) {
        return add(new MadImpl(name, date));
    }

    @Override
    public String getLabel() {
    	return "Mad";
    }
    
    @Override
    public Mad getByName(String name) {
        return getByKey(name);
    }

    @Override
    public List<Mad> activeMads() {
        List<Mad> mads = new LinkedList<Mad>(all());
        for (Iterator<Mad> iterator = mads.iterator(); iterator.hasNext();)
            if (!iterator.next().isActive())
                iterator.remove();
        return mads;
    }

	@Override
	public List<Team> getTeams() {
		return teams;
	}

	@Override
	public void createTeam(String teamName, List<String> mads) {
		Team team = new Team(teamName);
		for(String name: mads)
			team.addMad(getByName(name.trim()));
		teams.add(team);		
	}
}
