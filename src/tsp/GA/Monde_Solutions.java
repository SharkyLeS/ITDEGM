package tsp.GA;
import java.util.ArrayList;

import tsp.*;

public class Monde_Solutions {

	private ArrayList<Solution> monde_solutions;
	private ArrayList<Double> fitness; // Mémorise le coût de chacune des solutions (doit donc faire taille_monde)
	private int taille_Monde;
	private Instance m_instance;
	
	public Monde_Solutions(Instance i, int taille) throws Exception {
		m_instance=i;
		taille_Monde=taille;
		ArrayList<Solution> m = new ArrayList<Solution>();
		ArrayList<Double> f = new ArrayList<Double>();
		for(int j=0;j<taille_Monde;j++) {
			Solution s = new Solution(m_instance);
			m.add(s);   // Remplacer par les solutions obtenues par calcul du plusProcheVoisin sur différentes villes
			// Par conséquent, il est nécesaire que taille_monde<=m_instance.getNbCities()
			f.add(1/s.evaluate());  // on associe à chaque solution sa "fitness" = 1/son coût --> on cherche à trouver la soltuion de plus grande fitness
		}
		monde_solutions=m;
	}

	public Monde_Solutions(Instance i, int taille, ArrayList<Solution> monde) {
		m_instance=i;
		taille_Monde=taille;
		monde_solutions=monde;
	}

	public ArrayList<Solution> getMonde_solutions() {
		return monde_solutions;
	}

	public void setMonde_solutions(ArrayList<Solution> monde_solutions) {
		this.monde_solutions = monde_solutions;
	}

	public int getTaille_Monde() {
		return taille_Monde;
	}

	public void setTaille_Monde(int taille_Monde) {
		this.taille_Monde = taille_Monde;
	}

	public Instance getM_instance() {
		return m_instance;
	}

	public void setM_instance(Instance m_instance) {
		this.m_instance = m_instance;
	}
	
	public ArrayList<Double> getFitness() {
		return fitness;
	}

	public void setFitness(ArrayList<Double> fitness) {
		this.fitness = fitness;
	}
	
	/*
	 * retourne la probabilité de reproduction de la solution s dans les générations futures
	 */
	public double getProba(Solution s) throws Exception {
		double sum = 0;
		for(Solution sol : getMonde_solutions()) {
			sum+=1/sol.evaluate();
		}
		return (1/s.evaluate())/sum;
	}
	
	/*
	 *  Choisis 2 parents au sein du monde en fonction de leurs coûts (ceux de coût élevé ont moins de chance d'être choisis comme parents)
	 */
	public ArrayList<Solution> choisir_Parents() throws Exception {
		ArrayList<Solution> parents = new ArrayList<Solution>();
		ArrayList<Solution> occurences = new ArrayList<Solution>();
		// On crée une liste occurences ou on fera apparaitre chaque solution un nombre de fois proportionnel à sa probabilité
		for(Solution s : getMonde_solutions()) {
			int n = (int)(getProba(s)*100);
			for(int i=0;i<n;i++) occurences.add(s);
		}
		// On tire au hasard deux parents parmis la population
		int p1 = (int)(Math.random()*100);
		parents.add(occurences.get(p1));
		int p2 = (int)(Math.random()*100);
		parents.add(occurences.get(p2));
		return parents;
	}
}
