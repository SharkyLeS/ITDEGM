package tsp.AntColony;

import java.util.ArrayList;

import tsp.Instance;
import tsp.Solution;
import tsp.metaheuristic.AMetaheuristic;

public class AntAlgorithm extends AMetaheuristic {

	public static final boolean trace = false;
	public static final double alpha = 2.0; //pondere les phéromones A DEFINIR
	public static final double beta = 3.0; //pondere la visibilité
	public static final double rho = 0.9; // doit être entre 0 et 1
	public static final double Q = 1.0;
	
	private Solution bestSol;
	private double[][] proba;
	private double[][] visibilite; // à voir si on garde
	private double[][] pheromones; // les tau (i,j)	
	
	public AntAlgorithm(Instance instance, Solution sol) throws Exception {
		super(instance, "Colonie de fourmis");
		this.bestSol = sol;
		this.proba = new double[instance.getNbCities()][instance.getNbCities()];
		this.visibilite = new double[instance.getNbCities()][instance.getNbCities()];
		this.pheromones = new double[instance.getNbCities()][instance.getNbCities()];
		
		// initialise les matrices
		for (int i=0; i<instance.getNbCities(); i++) {
			for (int j=i+1; j<instance.getNbCities(); j++) {
				this.visibilite[i][j] = 1/(instance.getDistances(i, j));
				this.visibilite[j][i] = this.visibilite[i][j];
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
			this.proba[i][j] = Math.pow(getPheromones(i, j), alpha) * 
					Math.pow(getVisibilite(i,j),beta);
			acc += this.getProba(i, j);
		}
		for (int j : villesRest) { //on divise par le dénominateur, commun à toutes les probas
			this.setProba(getProba(i,j)/acc, i, j);
		}
	}

	//ChoixVille
	//getDeltaPheromones
	//majPheromones
	

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
		
		
		return sol;
	}
	
	public Solution lanceFourmi() throws Exception {
		return lanceFourmi(0);
	}

	public Solution solve(Solution sol) throws Exception {
		double meilleurDistance = sol.evaluate(); /*boucle présente ici, donc on mettre à jour 
		meilleurDistance si une meilleure solution est trouvée 
		Il va y avoir ici "Solution solActuelle = new Solution(instance)*/
		return null;
	}

}
