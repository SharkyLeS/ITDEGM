package tsp.GA;
import java.util.ArrayList;

import tsp.*;

public class GA{

	private ArrayList<Solution> monde_solutions;
	private ArrayList<Double> fitness; // Mémorise le coût de chacune des solutions (doit donc faire taille_monde)
	private int taille_Monde;
	private Instance m_instance;
	
	public GA(Instance i, int taille) throws Exception {
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

	public GA(Instance i, int taille, ArrayList<Solution> monde) {
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
	 *  D'autres implémentations sont envisageables pour comparer les performances
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
	
	/*
	 * Tire aléatoirement deux villes de la solution et les échange.
	 */
	public void mutation(Solution s) throws Exception {
		// Attention tout de même à ne pas changer la ville de départ/arrivée
		int p1 = (int)(1+Math.random()*(s.getInstance().getNbCities()-1));
		int p2 = (int)(1+Math.random()*(s.getInstance().getNbCities()-1));
		int v1 = s.getCity(p1);
		int v2 = s.getCity(p2);
		s.setCityPosition(v1, p2);
		s.setCityPosition(v2, p1);
	}
	
	/*
	 * Méthode de Crossover permettant de tester de nouvelles combinaisons (en combiant cette méthode avec les mutations)
	 * On implémente ici le MPX : Maximal Preservative Crossover
	 */
	public ArrayList<Solution> MPX(ArrayList<Solution> parents) throws Exception {
		int coupure = 0;
		int nb_Cities = parents.get(0).getInstance().getNbCities();
		if(nb_Cities<=10) coupure=nb_Cities/2;
		// On s'affranchit d'un cas de très petite instance par une modélisation basique
		else {
			coupure = 10+(int)(Math.random()*(nb_Cities-9));  
			// On choisit la coupure du crossover telle que : 10<=coupure<=nb_Cities/2
		}
		
		// On réalise le crossover
		Solution o1 = new Solution(m_instance);
		Solution o2 = new Solution(m_instance);
		ArrayList<Integer> cities1 = new ArrayList<Integer>();
		ArrayList<Integer> cities2 = new ArrayList<Integer>();
		ArrayList<Solution> offsprings = new ArrayList<Solution>();
		
		for(int i=0;i<nb_Cities;i++) {
			if(i<=coupure) {
				o1.setCityPosition(parents.get(0).getCity(i), i);
				cities1.add(parents.get(0).getCity(i));
				o2.setCityPosition(parents.get(1).getCity(i), i);
				cities2.add(parents.get(1).getCity(i));
			}
			else {
				int city1 =0;
				boolean trouve1=false;
				
				// On cherche les villes du parent 2 (en parcourant dans l'ordre) qui n'ont pas encore été répliquées dans le premier enfant
				while((city1<nb_Cities)&&(!trouve1)) {
					if(!cities2.contains(parents.get(1).getCity(city1))) {
						o1.setCityPosition(parents.get(1).getCity(city1), i);
						trouve1=true;
					}
					city1++;
				}
				
				// Même chose pour le deuxième enfant
				int city2=0;
				boolean trouve2=false;
				
				while((city2<nb_Cities)&&(!trouve2)) {
					if(!cities1.contains(parents.get(0).getCity(city2))) {
						o2.setCityPosition(parents.get(0).getCity(city2), i);
						trouve2=true;
					}
					city2++;
				}
			}	
		}
		offsprings.add(o1);
		offsprings.add(o2);
		return offsprings;
	}
}
