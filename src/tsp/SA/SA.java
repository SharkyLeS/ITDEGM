package tsp.SA;

import java.util.ArrayList;
import java.util.Collections;

import tsp.Instance;
import tsp.PlusProchesVoisins;
import tsp.Solution;
import tsp.heuristic.AHeuristic;
import tsp.metaheuristic.AMetaheuristic;
import tsp.opt.opt_2;

public class SA extends AMetaheuristic {
	
	private static final float T_rate = (float) 0.9999999999999999999999999999999999999999999;
	private static final double T0 = 10e5;
	private static final double T_lim = 10e-10;
	private static final double Kb = 1.380648e-23;
	private static final int nb_Temperatures=10000;
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
	}

	public ArrayList<Double> getTemperatures() {
		return temperatures;
	}

	@Override
	public Solution solve(Solution sol, long time) throws Exception {
		AHeuristic ini = (new PlusProchesVoisins(m_instance,"PlusProchesVoisins",time));
		ini.solve();
		Solution solutionIni2 = ini.getSolution();
		opt_2 Opt_2 = new opt_2(m_instance);
		sol = Opt_2.solve(solutionIni2, time).copy();
		
		ArrayList<Solution> sols = new ArrayList<Solution>();
		int k=0;
		double startTime = System.currentTimeMillis();
		double spentTime = 0.0;
		while((spentTime<time*1000)&&(k<1000)) {
			sols.add(this.LBCooling(sol, T0, T_rate, T_lim, time));
			spentTime = System.currentTimeMillis()-startTime;
		}
		
		Solution best_sol = sols.get(0);
		double best = sols.get(0).evaluate();
		
		for(Solution s : sols) {
			if(s.evaluate()<best) {
				best=s.evaluate();
				best_sol=s.copy();
			}
		}
		return best_sol;
	}

	/**
	 * Fonction codant le système de décision des solutions : 
	 * On génère une solution par swap à partir de sol, si elle est de coût inférieur à sol, on l'accepte
	 * sans condition, sinon, on l'accepte avec la probabilité exp(-différenceDeCout/T)
	 * Puis on fait décroître la température.
	 * On tente d'améliorer tant que la température est supérieure à la température limite et que le 
	 * temps limite n'a pas été dépassé
	 * On retourne la solution obtenue
	 * @param sol
	 * @param T0
	 * @param T_rate
	 * @param T_lim
	 * @param max_time
	 * @return
	 * @throws Exception
	 */
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
			spentTime = System.currentTimeMillis() - startTime;
		}
		return sol;
	}
	
	/**
	 * Code un swap entre deux villes de la solution s choisies au hasard
	 * renvoie la solution obtenue
	 * @param s
	 * @return
	 * @throws Exception
	 */
	public Solution swap(Solution s) throws Exception {
		int a = 1+(int)(Math.random()*(s.getInstance().getNbCities()-2));
		int b = a+1+(int)(Math.random()*(s.getInstance().getNbCities()-1-a));
		Solution sol = s.copy();
		sol.setCityPosition(s.getCity(b), a);
		sol.setCityPosition(s.getCity(a), b);
		return sol;
	}
	
	
	/**
	 * List-Based implementation du Recuit Simulé pour tenter de s'affranchir du 
	 * choix des paramètres, très dur et très changeant selon les instances.
	 * Le corps de la fonction repose sur le même principe que cooling()
	 * retourne la solution obtenue.
	 * @param x
	 * @param T0
	 * @param T_rate
	 * @param T_lim
	 * @param max_time
	 * @return
	 * @throws Exception
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
	
	/**
	 * Crée une première liste de températures pour les algos de cooling
	 * @param x
	 * @throws Exception
	 */
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
	
	/**
	 * Opérateur faisant la comparaison entre les solutions obtenues par les fonctions swap, insere et 
	 * inverse.
	 * rettourne la meilleure des 3 solutions
	 * @param s
	 * @return
	 * @throws Exception
	 */
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
	
	/**
	 * Code un swap entre deux villes i et j dans la solution s
	 * retourne la nouvelle solution obtenue
	 * @param s
	 * @param i
	 * @param j
	 * @return
	 * @throws Exception
	 */
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
	

	/**
	 * Place la ville de position j en position i et déplace conséquemment les autres villes.
	 * retourne la solution obtenue
	 * @param s
	 * @param i
	 * @param j
	 * @return
	 * @throws Exception
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
	
	/**
	 * Inverse le chemin entre les villes i et j dans la solution s 
	 * retourne la solution obtenue
	 * @param s
	 * @param i
	 * @param j
	 * @return
	 * @throws Exception
	 */
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
