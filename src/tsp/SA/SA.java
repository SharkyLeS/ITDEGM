package tsp.SA;

import java.util.ArrayList;
import java.util.Collections;

import tsp.Instance;
import tsp.Solution;
import tsp.metaheuristic.AMetaheuristic;

public class SA extends AMetaheuristic {
	
	private static final float T_rate = (float) 0.9999999999999999999999999999999999999999999;
	private static final double T0 = 10e5;
	private static final double T_lim = 10e-10;
	private static final double Kb = 1.380648e-23;
	private static final int nb_Temperatures=1000;
	private static final double p0 = 0.4;
	private static final long nb_Operations = (long) 1e50;
	
	private ArrayList<Double> temperatures;
	private double proba;
	
	public double getProba() {
		return proba;
	}

	public void setProba(double proba) {
		this.proba = proba;
	}

	public SA(Instance instance) throws Exception {
		super(instance, "Simulated Annealing");
		// TODO Auto-generated constructor stub
	}

	public ArrayList<Double> getTemperatures() {
		return temperatures;
	}

	@Override
	public Solution solve(Solution sol, long time) throws Exception {
		return this.LBCooling(sol, T0, T_rate, T_lim, time);
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
		int a = 1+(int)(Math.random()*(s.getInstance().getNbCities()-2));
		int b = a+1+(int)(Math.random()*(s.getInstance().getNbCities()-1-a));
		Solution sol = s.copy();
		sol.setCityPosition(s.getCity(b), a);
		sol.setCityPosition(s.getCity(a), b);
		return sol;
	}
	
	/*
	 * List-Based implementation du Recuit Simuler pour tenter de s'affranchir du 
	 * choix des paramètres, très dur et très changeant selon les instances.
	 */
	public Solution LBCooling(Solution x, double T0, double T_rate, double T_lim, double max_time) throws Exception {
		int k=0;
		this.InitialiseTemperatures(x);
		double startTime = System.currentTimeMillis();
		double spentTime = 0;
		while((k<nb_Operations)&&(spentTime<max_time)) {
			int m=0;
			int c=0;
			double t=0;
			while(m<x.getInstance().getNbCities()) {
				Solution y = this.HybridGreedyOperator(x);
				if(y.evaluate()<=x.evaluate()) {
					x=y.copy();
				}
				else {
					double p = Math.exp((x.evaluate()-y.evaluate())/getTemperatures().get(0));
					double r = Math.random();
					if(r<=p) {
						t+=(x.evaluate()-y.evaluate())/Math.log(r);
						c++;
						x=y.copy();
					}
				}
				m++;
				spentTime = System.currentTimeMillis()-startTime;
			}
			if(c!=0) {
				getTemperatures().remove(0);
				getTemperatures().add(t/c);
			}
			k++;
			spentTime = System.currentTimeMillis()-startTime;
		}
		return x;
	}
	
	public void InitialiseTemperatures(Solution x) throws Exception {
		ArrayList<Double> L = new ArrayList<Double>();
		while(L.size()<nb_Temperatures) {
			Solution y;
			do {
				y = this.HybridGreedyOperator(x);
			}
			while(y.evaluate()<=x.evaluate());
			double t = (x.evaluate()-y.evaluate())/Math.log(getProba());
			//System.err.println(t);
			L.add(t);
		}
		Collections.sort(L);
		temperatures=L;
	}
	
	public Solution HybridGreedyOperator(Solution s) throws Exception {
		int i = 1+(int)(Math.random()*(s.getInstance().getNbCities()-2));
		int j = i+1+(int)(Math.random()*(s.getInstance().getNbCities()-1-i));
		Solution swap = this.swap(s,i,j);
		Solution insert = this.insert(s, i, j);
		Solution inverse = this.inverse(s, i, j);
		if(swap.evaluate()<insert.evaluate()) {
			if(swap.evaluate()<inverse.evaluate()) {
				return swap;
			}
			else {
				return inverse;
			}
		}
		else {
			if(insert.evaluate()<inverse.evaluate()) {
				return insert;
			}
			else {
				return inverse;
			}
		}
	}
	
	public Solution swap(Solution s, int i, int j) throws Exception {
		int n = s.getInstance().getNbCities();
		if((0>=i)||(0>=j)||(i>=n)||(j>=n)||(i==j)) {
			throw new Exception("Il est nécessaire que i et j appartiennent à l'intervalle [1;"+n+"] et que i soit différent de j");
		}
		else {
			Solution sol = s.copy();
			sol.setCityPosition(s.getCity(j), i);
			sol.setCityPosition(s.getCity(i), j);
			return sol;
		}
	}
	
	/*
	 * Place la ville de position j en position i
	 */
	public Solution insert(Solution s, int i, int j) throws Exception {
		int n = s.getInstance().getNbCities();
		if((0>=i)||(0>=j)||(i>=n)||(j>=n)||(i==j)) {
			throw new Exception("Il est nécessaire que i et j appartiennent à l'intervalle [1;"+n+"] et que i soit différent de j");
		}
		else {
			Solution sol = s.copy();
			if(i<j) {
				sol.setCityPosition(s.getCity(j), i);
				for(int k=1;k<=(j-i);k++) {
					sol.setCityPosition(s.getCity(i+k-1), i+k);
				}
			}
			else {
				sol.setCityPosition(s.getCity(j), i);
				for(int k=1;k<=(i-j);k++) {
					sol.setCityPosition(s.getCity(i-k+1), i-k);
				}
			}
			return sol;
		}
	}
	
	public Solution inverse(Solution s, int i, int j) throws Exception {
		int n = s.getInstance().getNbCities();
		if((0>=i)||(0>=j)||(i>=n)||(j>=n)||(i==j)) {
			throw new Exception("Il est nécessaire que i et j appartiennent à l'intervalle [1;"+n+"] et que i soit différent de j");
		}
		else {
			Solution newSol = s.copy();
			int debut = Math.min(i, j);
			int fin = Math.max(i, j);
			for(int k=0;k<(fin+1-debut)/2;k++) {
				int a = s.getCity(debut+k);
				int b = s.getCity(fin-k);
				newSol.setCityPosition(b, debut+k);
				newSol.setCityPosition(a, fin-k);
			}
			return newSol;
		}
	}
}
