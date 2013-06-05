package mpa.main.loader;

public class ConfigStream {
	
	private String teams;
	private String expectedPairs;
	
	public ConfigStream(String teams, String expectedPairs) {
		this.teams = teams;
		this.expectedPairs = expectedPairs;
	}

	public String getTeamsStream() {
		return teams;
	}
	
	public String getExpectedPairsStream(){
		return expectedPairs;
	}
}
