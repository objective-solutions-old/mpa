package mpa.main.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;

import mpa.mad.Mad;
import mpa.mad.MadHome;
import mpa.pair.Pair;
import mpa.pair.PairHome;
import mpa.search.evaluator.PredefinedPairs;

public class ExpectedLoaderFile implements ExpectedLoader {

	private PredefinedPairs predefinedPairs;
	private ConfigFiles configFile;
	private Scanner scanner;
	private MadHome madHome;
	private PairHome pairHome;

	public ExpectedLoaderFile(PredefinedPairs predefinedPairs, ConfigFiles configFile, MadHome madHome, PairHome pairHome){
		this.predefinedPairs = predefinedPairs;
		this.configFile = configFile;
		this.madHome = madHome;
		this.pairHome = pairHome;
	}
	
	public void load() {
		try {
			loadExpectedPairs();
		} catch (Exception e) {
			if (scanner.hasNextLine())
				throw new RuntimeException(scanner.nextLine(), e);
			throw new RuntimeException(e);
		}
	}
	
	private void loadExpectedPairs() throws IOException {
		initializeScanner(configFile.getExpectedPairsFile());
		while (scanner.hasNext()) {
			String operation = scanner.next();
			if ("+".equals(operation))
				predefinedPairs.expectPair(readPair());
			else if ("-".equals(operation))
				predefinedPairs.rejectPair(readPair());
			else
				throw new RuntimeException("Expected '+' or '-' but found " + operation);
		}
	}
	
	private void initializeScanner(URL file) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(file.openStream()));
		scanner = new Scanner(reader);
	}
	
	private Pair readPair() {
		String[] mads = scanner.nextLine().split("/");
		return pairHome.createPair(getMad(mads[0].trim()), getMad(mads[1].trim()));
	}
	
	private Mad getMad(String name) {
		return madHome.getByName(name);		
	}
}
