package tsp.metaheuristic;

import tsp.Solution;

public class ThreadPerso implements Runnable {
	
	private AMetaheuristic AM;
	private Solution ini;
	
	public ThreadPerso(AMetaheuristic AMeta) {
		this.AM = AMeta;
		//this.ini = PlusProcheVoisin
	}
	
	public ThreadPerso(AMetaheuristic AMeta, Solution sol) {
		this.AM=AMeta;
		this.ini=sol;
	}
	
	public void run() {
		try {
			AM.solve(this.ini,50000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
