package mpa.main.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import mpa.entity.EntityNotFound;
import mpa.mad.Mad;
import mpa.mad.MadHome;
import mpa.pair.Pair;
import mpa.pair.PairHome;
import mpa.scenary.Scenary;
import mpa.scenary.ScenaryHome;

public class MpaLoader {

	private final MadHome madHome;
	private final ScenaryHome scenaryHome;
	private final PairHome pairHome;
	private final ConfigFiles configFile;
	private Scanner scanner;
	private Scenary currentScenary;

	public MpaLoader(MadHome madHome, ScenaryHome scenaryHome, PairHome pairHome, ConfigFiles mpaFile) {
		this.madHome = madHome;
		this.scenaryHome = scenaryHome;
		this.pairHome = pairHome;
		this.configFile = mpaFile;
	}

	private void initializeScanner(URL file) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(file.openStream()));
		scanner = new Scanner(reader);
	}

	public void load() {
		try {
			initializeScanner(configFile.getMpaFile());
			int mpaCount = scanner.nextInt();
			while (mpaCount-- > 0)
				readScenary();
		} catch (Exception e) {
			if (scanner.hasNextLine())
				throw new RuntimeException(scanner.nextLine(), e);
			throw new RuntimeException(e);
		}
	}

	private void readScenary() {
		currentScenary = scenaryHome.createScenary(scanner.nextInt(), readDate());
		readPairs();
	}

	private Date readDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(scanner.nextInt(), scanner.nextInt() - 1, scanner.nextInt());
		Date startDate = calendar.getTime();
		return startDate;
	}

	private void readPairs() {
		int scenaryCount = scanner.nextInt();
		for (int i = 0; i < scenaryCount; i++) {
			scanner.nextInt();
			currentScenary.addPair(readPair());
		}
	}

	private Pair readPair() {
		String[] mads = scanner.nextLine().split("/");
		return pairHome.createPair(getOrCreateMad(mads[0].trim()), getOrCreateMad(mads[1].trim()));
	}

	private Mad getOrCreateMad(String name) {
		try {
			return madHome.getByName(name);
		} catch (EntityNotFound e) {
			return madHome.createMad(name, currentScenary.getStartDate());
		}
	}
}
