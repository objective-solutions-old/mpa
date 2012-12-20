package mpa.main.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import mpa.mad.MadHome;

public class TeamLoader {

	private MadHome madHome;
	private ConfigFiles configFile;
	private Scanner scanner;

	public TeamLoader(MadHome madHome, ConfigFiles configFile) {
		this.madHome = madHome;
		this.configFile = configFile;
	}

	public void load() {
		try {
			initializeScanner(configFile.getTeamsFile());
			while (scanner.hasNext())
				createTeam();
		} catch (Exception e) {
			if (scanner.hasNextLine())
				throw new RuntimeException(scanner.nextLine(), e);
			throw new RuntimeException(e);
		}
	}

	private void initializeScanner(URL file) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(file.openStream()));
		scanner = new Scanner(reader);
	}

	private void createTeam() {
		List<String> names = nextTeamNames();
		String teamName = names.remove(0);
		madHome.createTeam(teamName, names);
	}

	private List<String> nextTeamNames() {
		return new LinkedList<String>(Arrays.asList(scanner.nextLine().split(";")));
	}

}
