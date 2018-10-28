package tsp.GA;
import java.util.ArrayList;

import tsp.*;
import tsp.metaheuristic.AMetaheuristic;

public class GA extends AMetaheuristic{

	private ArrayList<Solution> monde_solutions;
	private ArrayList<Double> fitness; // Mémorise le coût de chacune des solutions 
	// (doit donc faire taille_monde)
	private int taille_Monde;
	private long timeLimit;
	// Prportion de la population qui doit être renouvelée dans génération suivante
	public static final double Success_Ratio = 0.9;
	// Nombre maximal de fils à générer à chaque itération pour créer gén suivante
	// (Multiple de la taille de la population)
	public static final double Max_Selection_Pressure = 25;
	public static final double Proba_Mutation = 0.15;
	
	public GA(Solution ini, Instance i, int taille, long timeLimit) throws Exception {
		super(i,"Genetic Algorithm");
		taille_Monde=taille;
		ArrayList<Solution> m = new ArrayList<Solution>();
		m.add(ini);
		ArrayList<Double> f = new ArrayList<Double>();
		for(int j=1;j<taille_Monde;j++) {
			Solution s = this.mutation(ini);
			//s.print(System.err);
			m.add(s);   // Remplacer par les solutions obtenues par calcul du plusProcheVoisin sur différentes villes
			f.add(1/s.evaluate());  // on associe à chaque solution sa "fitness" = 1/son coût --> on cherche à trouver la soltuion de plus grande fitness
		}
		this.monde_solutions=m;
		this.timeLimit=timeLimit;
		this.fitness = f;
	}

	public GA(Instance i, int taille, ArrayList<Solution> monde, long timeLimit) throws Exception {
		super(i,"Genetic Algorithm");
		taille_Monde=taille;
		monde_solutions=monde;
		this.timeLimit=timeLimit;
	}

	public void addSolution_Monde(Solution s) {
		this.monde_solutions.add(s);
		this.setTaille_Monde(getTaille_Monde()+1);
	}
	
	public ArrayList<Solution> getMonde_solutions() {
		return monde_solutions;
	}

	public void setMonde_solutions(ArrayList<Solution> monde_solutions) {
		this.monde_solutions = monde_solutions;
		this.setTaille_Monde(monde_solutions.size());
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
	 * Représente la valeur de l'incrément de lambda à chaque itération 
	 * Permet à lambda de croître graduellement de 0 à 1
	 * 
	 * Valeur à choisir précisemment (y reréflechir)
	 */
	public double getTaux_Lambda() {
		//return 1/(this.getTaille_Monde()*Max_Selection_Pressure);
		return 1/this.getTaille_Monde(); // Valeur pour tests
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
	 *  
	 *  Algorithme coûteux à cause de precision_Proba très grande 
	 */
	public ArrayList<Solution> choisir_Parents() throws Exception {
		ArrayList<Solution> parents = new ArrayList<Solution>();
		ArrayList<Solution> occurences = new ArrayList<Solution>();
		int precision_Proba = (int) 1e6; // Très grande précision nécessaire dûe aux probas très petites
		// On crée une liste occurences ou on fera apparaitre chaque solution un nombre de fois proportionnel à sa probabilité
		for(Solution s : getMonde_solutions()) {
			int n = (int)(getProba(s)*precision_Proba);
			for(int i=0;i<n;i++) occurences.add(s);
		}
		// On tire au hasard deux parents parmis la population
		int p1 = (int)(Math.random()*precision_Proba);
		parents.add(occurences.get(p1));
		int p2 = (int)(Math.random()*precision_Proba);
		parents.add(occurences.get(p2));
		return parents;
	}
	
	/*
	 * Autre implémentation de choix des parents dans une population.
	 * On implémente ici la sélection par "tournoi" : on choisit à chaque fois deux
	 * membres au hasard que l'on compare en choisissant celui de coût moindre.
	 */
	public ArrayList<Solution> choisirParentsTournoi() throws Exception{
		ArrayList<Solution> parents = new ArrayList<Solution>();
		for(int i=0;i<2;i++) {
			int i1 = 1+(int)(Math.random()*(this.getTaille_Monde()-1));
			int i2 = 1+(int)(Math.random()*(this.getTaille_Monde()-1));
			Solution p1 = this.getMonde_solutions().get(i1);
			Solution p2 = this.getMonde_solutions().get(i2);
			if(p1.evaluate()<=p2.evaluate()) parents.add(p1);
			else parents.add(p2);
		}
		return parents;
	}
	
	
	/*
	 * Tire aléatoirement deux villes de la solution et les échange.
	 */
	public Solution mutation(Solution s) throws Exception {
		// Attention tout de même à ne pas changer la ville de départ/arrivée
		Solution sol = s.copy();
		int p1 = (int)(1+Math.random()*(s.getInstance().getNbCities()-1));
		int p2 = (int)(1+Math.random()*(s.getInstance().getNbCities()-1));
		int v1 = sol.getCity(p1);
		int v2 = sol.getCity(p2);
		sol.setCityPosition(v1, p2);
		sol.setCityPosition(v2, p1);
		return sol;
	}
	
	/*
	 * Méthode de Crossover permettant de tester de nouvelles combinaisons (en combiant cette méthode avec les mutations)
	 * Renvoie des fils issus de parents, générés par le crossover MPX
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
					if(!cities1.contains(parents.get(1).getCity(city1))) {
						o1.setCityPosition(parents.get(1).getCity(city1), i);
						cities1.add(parents.get(1).getCity(city1));
						trouve1=true;
					}
					city1++;
				}
				
				// Même chose pour le deuxième enfant
				int city2=0;
				boolean trouve2=false;
				
				while((city2<nb_Cities)&&(!trouve2)) {
					if(!cities2.contains(parents.get(0).getCity(city2))) {
						o2.setCityPosition(parents.get(0).getCity(city2), i);
						cities2.add(parents.get(0).getCity(city2));
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
	
	/*
	 * Retourne true si le fils o a un coût inférieur ou égal à ses parents, false sinon
	 */
	public boolean isElligible(Solution o, ArrayList<Solution> parents, double lambda) throws Exception {
		double objective_fitness = 0;
		double p1_fitness = parents.get(0).evaluate();
		double p2_fitness = parents.get(1).evaluate();
		// On codera lambda croissant graduellement de 0 à 1 
		if(p1_fitness>=p2_fitness) objective_fitness = (1-lambda)*p1_fitness + lambda*p2_fitness;
		else objective_fitness = (1-lambda)*p2_fitness + lambda*p1_fitness;
		return o.evaluate()<=objective_fitness;
	}
	
	/*
	 * On code ici tout le processus de séléction de la génération suivante, en faisant appel aux autres méthodes de la classe
	 * Retourne une nouvelle génération
	 */
	public ArrayList<Solution> offspring_Selection(double lambda) throws Exception{
		int i=0;
		ArrayList<Solution> offsprings_Elligibles = new ArrayList<Solution>();
		ArrayList<Solution> offsprings_Rejetes = new ArrayList<Solution>();
		while((offsprings_Elligibles.size()<=Success_Ratio*this.getTaille_Monde())&&(i<=Max_Selection_Pressure*this.getTaille_Monde())) {
			/* Tant que l'on a pas géneré Success_Ratio*this.getTaille_Monde()
			 * fils valables à partir de la génération, on choisit des parents, on génère
			 * deux fils à partir de crossover, on leur applique des mutations avec la
			 * probabilité proba_Mutation et on les choisit pour la nouvelle génération
			 * si leur coût est meilleur que les parents.
			 */
			ArrayList<Solution> parents = this.choisirParentsTournoi();
			ArrayList<Solution> offsprings = this.MPX(parents);
			for(Solution o : offsprings) {
				double p = Math.random();
				if(p<=Proba_Mutation) o = this.mutation(o);
				if(isElligible(o, parents, lambda)) offsprings_Elligibles.add(o);
				else offsprings_Rejetes.add(o);
			}
			i+=2;
		}
		if(offsprings_Elligibles.size()<Success_Ratio*this.getTaille_Monde()) System.err.println("Convergence prématurée");
		/*
		 * On remplit la nouvelle génération avec les elligibles et 
		 * (i-offsprings_Elligibles.size()) non elligibles
		 */
		ArrayList<Solution> new_Gen = new ArrayList<Solution>();
		new_Gen.addAll(offsprings_Elligibles);
		for(int j=0;j<this.getTaille_Monde()-offsprings_Elligibles.size();j++) {
			new_Gen.add(offsprings_Rejetes.get(j));
		}
		
		return new_Gen;
	}

	@Override
	public Solution solve(Solution sol, long time) throws Exception {
		/*
		 * On fait GA tant que timeLimit n'a pas été atteint.
		 */
		double lambda =0;
		long startTime = System.currentTimeMillis();
		long spentTime=0;
		while(spentTime<getTimeLimit()*1000-100) {
			this.setMonde_solutions(this.offspring_Selection(lambda));
			spentTime = System.currentTimeMillis()-startTime;
			lambda += this.getTaux_Lambda();
		}
		return this.trouveOptimum();
	}

	public long getTimeLimit() {
		return timeLimit;
	}
	
	// Choisis la solution de moindre coût dans le monde
	public Solution trouveOptimum() throws Exception {
		Solution opti=this.getMonde_solutions().get(0);
		double best_sol = this.getMonde_solutions().get(0).evaluate();
		for(Solution s : this.getMonde_solutions()) {
			if(s.evaluate()<=best_sol) {
				opti=s;
				best_sol=s.evaluate();
			}
		}
		return opti;
	}
}
