package mpa.mad;

import java.util.Date;
import java.util.List;

import mpa.entity.EntityHome;

public interface MadHome extends EntityHome<Mad> {

    Mad createMad(String name, Date date);

    Mad getByName(String name);

    List<Mad> activeMads();

	List<Team> getTeams();

	void createTeam(String name, List<String> mads);
}
