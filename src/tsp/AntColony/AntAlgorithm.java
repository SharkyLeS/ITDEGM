package tsp.AntColony;

import java.util.ArrayList;

import tsp.Instance;
import tsp.Solution;
import tsp.metaheuristic.AMetaheuristic;

public class AntAlgorithm extends AMetaheuristic {

	public static final boolean trace = true;
	public static final double alpha = 1.0; //pondere les phéromones A DEFINIR
	public static final double beta = 1.0; //pondere la visibilité
	public static final double rho = 0.5; // doit être entre 0 et 1
	public static final double Q = 100.0;
	
	private Solution bestSol;
	private double[][] proba;
	private double[][] visibilite; // à voir si on garde
	private double[][] pheromones; // les tau (i,j)	
	
	public AntAlgorithm(Instance instance, Solution sol) throws Exception {
		super(instance, "Colonie de fourmis");
		this.bestSol = sol.copy();
		this.proba = new double[instance.getNbCities()][instance.getNbCities()];
		this.visibilite = new double[instance.getNbCities()][instance.getNbCities()];
		this.pheromones = new double[instance.getNbCities()][instance.getNbCities()];
		
		// initialise les matrices
		for (int i=0; i<instance.getNbCities(); i++) {
			for (int j=i+1; j<instance.getNbCities(); j++) {
				this.visibilite[i][j] = 1.0/(instance.getDistances(i, j));
				this.visibilite[j][i] = this.visibilite[i][j];
				//if (trace) System.out.println("distance entre " + i + " et " + j +" : "+instance.getDistances(i, j));
				//if (trace) System.out.println("visibilité entre " + i + " et " + j +" : "+this.visibilite[i][j]);
				this.pheromones[i][j] = 0;
				this.pheromones[j][i] = 0;
			}
			this.visibilite[i][i] = 0;
			this.pheromones[i][i] = 0;
		}
	}
	
	// GETTERS AND SETTERS
	public Solution getBestSol() {
		return this.bestSol;
	}
	public void setBestSol(Solution sol) {
		this.bestSol = sol;
	}
	public double getProba(int i,int j) {
		return this.proba[i][j];
	}
	public void setProba(double valeur,int i, int j) {
		this.proba[i][j] = valeur;
	}
	public double getVisibilite(int i,int j) {
		return visibilite[i][j];
	}
	
	public void setVisibilite(double valeur,int i, int j) {
		this.visibilite[i][j] = valeur;
	}
	public double getPheromones(int i,int j) {
		return pheromones[i][j];
	}
	public void setPheromones(double valeur, int i, int j) {
		this.pheromones[i][j] = valeur;
	}
	

	//Met à jour this.proba autour de i ; tourne avant de choisir une ville
	public void majProba(int i, ArrayList<Integer> villesRest) {
		double acc = 0.0;
		for (int j : villesRest) { // d'abord le numérateur
			double termeAlpha = Math.pow(getPheromones(i, j), alpha);
			double termeBeta = Math.pow(getVisibilite(i,j),beta);
			
			this.proba[i][j] = termeAlpha * termeBeta; 
			
			if (!trace) {
				System.out.println("A modifier : de " + i + " à " +j);
				System.out.println("terme en alpha : " +termeAlpha);
				System.out.println("terme en beta : " +termeBeta);
				System.out.println("donc le terme est " + this.proba[i][j]);
				System.out.println("----------------------------");
			}
			
			acc += this.getProba(i, j);
		}
		for (int j : villesRest) { //on divise par le dénominateur, commun à toutes les probas
			this.setProba(getProba(i,j)/acc, i, j);
		}
	}

	public int choixVille(int i, ArrayList<Integer> villesRest) throws Exception {
		double x = Math.random();
		double acc = 0.0;
		int villeChoisie = -1;
		for (int j : villesRest) { // amélioration : retirer ici villeChoisie de villesRest
			if (x<acc+ this.proba[i][j]) {
				villeChoisie = j;
				break;
			} else if (j == villesRest.get(villesRest.size()-1)) {
				villeChoisie = j;
				break;
			} else {
				//if (trace) System.out.println("avant : " +acc);
				acc += this.proba[i][j];
				//if (trace) System.out.println("apres : " +acc);
			}
		}
		return villeChoisie;
	}
	
	
	public String toStringMatrice(double[][] mat) {
		String s = "[";
		for (int i=0; i<mat.length; i++) {
			s+= "[";
			for (int j=0; j<mat[0].length; j++) {
				s+= mat[i][j] + " , ";
			}
			s+= "] ," + "\n";
		}
		return s;
	}
	
	public double getDeltaPheromones(double longueur) throws Exception { //Max
		return Q/longueur;
	}
	
	public void majPheromones(Solution s, double longueur) throws Exception {
		for (int i=0;i<super.getInstance().getNbCities()-1;i++) {
			this.setPheromones(rho*this.getPheromones(s.getCity(i),s.getCity(i+1))+
					this.getDeltaPheromones(longueur), s.getCity(i) , s.getCity(i+1));
			//amélio : ne pas mettre de pheromones sur les derniers arcs ?
		}		
	}

	public Solution lanceFourmi(int villeDeDepart) throws Exception {
		Solution sol = new Solution(super.getInstance());
		// Initialisation
		sol.setCityPosition(villeDeDepart, 0);
		sol.setCityPosition(villeDeDepart, super.getInstance().getNbCities());
		ArrayList<Integer> villesRestantes = new ArrayList<Integer>();
		for (int i=0; i<super.getInstance().getNbCities(); i++) {
			if (i!=villeDeDepart) villesRestantes.add(i);
		}
		// Boucle
		int i = villeDeDepart;
		for(int position=1 ; position<super.getInstance().getNbCities();position++) {
			majProba(i,villesRestantes);
			i = choixVille(i,villesRestantes);
			sol.setCityPosition(i, position);
			for(int j=0;j<villesRestantes.size();j++) { //retire i de villesRestantes
				if(villesRestantes.get(j)==i) {
					villesRestantes.remove(j);
					break;
				}
			}
		}
		majPheromones(sol,sol.evaluate());
		return sol;
	}
	
	public Solution lanceFourmi() throws Exception {
		return lanceFourmi(0);
	}

	public Solution solve(Solution sol,long time) throws Exception {
		long startTime = System.currentTimeMillis();
		
		int i=0;
		Solution solActuelle;
		while (System.currentTimeMillis() - startTime<time*1000-100) {
			solActuelle = lanceFourmi(i);
			System.out.println("la 1ere fourmi a tourné");
			if (solActuelle.getObjectiveValue()<sol.getObjectiveValue()) {
				sol = solActuelle.copy();
			}
			
		}
		return sol;
	}

}
