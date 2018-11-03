package tsp.metaheuristic;

import java.util.concurrent.Callable;

import tsp.Solution;

public class ThreadPerso implements Callable {
	
	private AMetaheuristic AM;
	private Solution ini;
	private long time;
	private long timeLimit;
	
	public ThreadPerso(AMetaheuristic AMeta) {
		this.AM = AMeta;
		//this.ini = PlusProcheVoisin
	}
	
	public ThreadPerso(AMetaheuristic AMeta, Solution sol, long t, long tLimit) {
		this.AM=AMeta;
		this.ini=sol;
		this.time = t;
		this.timeLimit = tLimit;
	}
	
	public void run() {
		try {
			Solution s = AM.solve(this.ini, timeLimit-time-5);
			ini = s.copy();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Solution getSolution() {
		return this.ini;
	}

	public Solution call() throws Exception {
		try {
			Solution s = AM.solve(this.ini, timeLimit-time);
			return s;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
