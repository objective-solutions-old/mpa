package mpa.main.loader;

import org.picocontainer.Startable;

public class Loader implements Startable{

	private TeamLoader teamLoader;
	private MpaLoader mpaLoader;
	private ExpectedLoader expectedLoader;

	public Loader(TeamLoader teamLoader, MpaLoader mpaLoader, ExpectedLoader expectedLoader){
		this.teamLoader = teamLoader;
		this.mpaLoader = mpaLoader;
		this.expectedLoader = expectedLoader;		
	}

	@Override
	public void start() {
		mpaLoader.load();
		teamLoader.load();
		expectedLoader.load();
	}

	@Override
	public void stop() {
	}	
	
}
