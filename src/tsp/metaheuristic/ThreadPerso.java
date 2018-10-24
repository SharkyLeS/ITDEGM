package tsp.metaheuristic;

import tsp.Solution;

public class ThreadPerso implements Runnable {
	
	private AMetaheuristic AM;
	private Solution ini;
	
	public ThreadPerso(AMetaheuristic AMeta) {
		this.AM = AMeta;
		//this.ini = PlusProcheVoisin
	}
	
	
	public void run() {
		try {
			AM.solve(this.ini);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
