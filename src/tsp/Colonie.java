package tsp;

import java.util.ArrayList;

import tsp.metaheuristic.AMetaheuristic;

public class Colonie extends AMetaheuristic {
	private double alpha; //pondere les phéromones
	private double beta; //pondere la visibilité
	private double rho; //
	private int Q;
	private double[][] visibilite;
	private double[][] pheromones; // les tau (i,j)
	private static Instance instance;

	public Colonie() throws Exception {
		super(instance, "Colonie de fourmis");
	}
	
	public Colonie(Instance instance,double alph, double bet, double rh, int q) throws Exception {
		super(instance, "Colonie de fourmi");
		this.alpha = alph;
		this.beta = bet;
		this.rho = rh;
		this.Q=q;
		this.pheromones = new double[instance.getNbCities()][instance.getNbCities()];
		visibilite = new double[instance.getNbCities()][instance.getNbCities()];
		
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

	public double getAlpha() {
		return alpha;
	}

	public double getBeta() {
		return beta;
	}

	public double getRho() {
		return rho;
	}

	public int getQ() {
		return this.Q;
	}
	
	public double[][] getVisibilite() {
		return visibilite;
	}
	
	public double getVisibilite(int i, int j) {
		return visibilite[i][j];
	}

	// ne pas priviliégier ce getter
	public double[][] getPheromones() {
		return pheromones;
	}
	
	public double getPheromones(int i,int j) {
		return pheromones[i][j];
	}

	public void setPheromones(double[][] pheromones) {
		this.pheromones = pheromones;
	}
	
	public void setPheromones(double valeur, int i, int j) {
		this.pheromones[i][j] = valeur;
	}
	
	public ArrayList<Integer> lanceFourmi(int villeDeDepart) throws Exception {
		Fourmi f = new Fourmi(villeDeDepart);
		while (f.getVillesRestantes().size()!=0) {
			f.majProba();
			f.choixVille();
		}
		f.majPheromones();
		f.getVillesParcourues().add(f.getVillesParcourues().get(0));
		return f.getVillesParcourues(); 
	}

	public Solution solve(Solution sol) throws Exception {
		
		return null;
	}

	@Override
	public Solution solve(Solution sol, long time) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
