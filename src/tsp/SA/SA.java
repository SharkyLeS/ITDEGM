package tsp.SA;

import tsp.Instance;
import tsp.Solution;
import tsp.metaheuristic.AMetaheuristic;

public class SA extends AMetaheuristic {
	
	private static final float T_rate = (float) 0.9999999999999999999999999999999999999999999;
	private static final double T0 = 10e5;
	private static final double T_lim = 10e-10;
	private static final double Kb = 1.380648e-23;
	

	public SA(Instance instance) throws Exception {
		super(instance, "Simulated Annealing");
		// TODO Auto-generated constructor stub
	}

	@Override
	public Solution solve(Solution sol, long time) throws Exception {
		return this.cooling(sol, T0, T_rate, T_lim, time);
	}

	public Solution cooling(Solution sol, double T0, double T_rate, double T_lim, long max_time) throws Exception {
		double T=T0;
		double startTime = System.currentTimeMillis();
		double spentTime = 0;
		while((T>T_lim)&&(spentTime<max_time)) {
			Solution s = this.swap(sol);
			if(s.evaluate()<=sol.evaluate()) sol=s.copy();
			else {
				double delta_E = s.evaluate()-sol.evaluate();
				double p = Math.exp(-delta_E/T);
				double x = Math.random();
				if(x<=p) sol=s.copy();
			}
			T*=T_rate;
			spentTime = System.currentTimeMillis()-startTime;
		}
		return sol;
	}
	
	public Solution swap(Solution s) throws Exception {
		int a = 1+(int)(Math.random()*(s.getInstance().getNbCities()-3));
		int b = a+1+(int)(Math.random()*(s.getInstance().getNbCities()-2-a));
		Solution sol = s.copy();
		sol.setCityPosition(s.getCity(b), a);
		sol.setCityPosition(s.getCity(a), b);
		return sol;
	}
}
