package mpa.main.loader;

import java.net.URL;

public class ConfigFiles {

	public URL getMpaFile() {
		return getClass().getResource("/mpas.txt");
	}
	
	public URL getTeamsFile() {
		return getClass().getResource("/teams.txt");
	}
	
	public URL getExpectedPairsFile(){
		return getClass().getResource("/expected.txt");
	}
}
