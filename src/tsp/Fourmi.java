package tsp;

import java.util.ArrayList;
import java.util.List;

public class Fourmi extends Colonie {
	private ArrayList<Integer> villesParcourues;
	private Double[][] proba; //Matrice symétrique des probabilités, partiellement mise à jour à chaque étape
	private ArrayList<Integer> villesRestantes;
	
	
	public Fourmi(int villeDeDepart) {
		super(); // A VOIR
		this.villesParcourues = new ArrayList<Integer>();
		this.villesParcourues.add(villeDeDepart);
		this.proba = new Double[super.getInstance().getNbCities()][super.getInstance().getNbCities()];
		this.villesRestantes = new ArrayList<Integer>();
		for(int i=0;i<super.getInstance().getNbCities();i++) {
			if (i != villeDeDepart) {
				this.villesRestantes.add(i);
			}
		}
	}
	
	//Met à jour this.proba autour de i ; tourne avant de choisir une ville
	public void majProba() {
		int i = this.villesParcourues.get(this.villesParcourues.size()-1);
		double acc = 0.0;
		for (int j : this.villesRestantes) { // d'abord le numérateur
			this.proba[i][j] = Math.pow(super.getPheromones(i, j), super.getAlpha()) * 
					Math.pow(super.getVisibilite(i,j), super.getBeta());
			acc += this.proba[i][j];
		}
		for (int j : this.villesRestantes) { //on divise par le dénominateur, commun à toutes les probas
			this.proba[i][j] = this.proba[i][j]/acc;
		}
	}
	
	/*avec le math.random et les probas
	 * Comme la somme des probas vaut 1, on fait des intervalles en sommant (voir dessin)
	 * Est-ce que ce sera exactement ? -> on prend le dernier
	 * 
	 * + MET A JOUR VILLES RESTANTES ET VILLES PARCOURUES*/
	public void choixVille() throws Exception {
		int i = this.villesParcourues.get(this.villesParcourues.size()-1);
		double x = Math.random();
		double acc = 0;
		int villeChoisie = 0;
		for (int j : this.villesRestantes) {
			if (x<acc+ this.proba[i][j]) {
				villeChoisie = j;
				break;
			} else if (j == this.villesRestantes.get(this.villesRestantes.size()-1)) {
				villeChoisie = j;
				break;
			} else {
				acc += this.proba[i][j];
			}
		}
		removeDisponibleCity(this.villesRestantes, villeChoisie);
		this.villesParcourues.add(villeChoisie);
	}
	
	
	public void removeDisponibleCity(ArrayList<Integer> idRestants, int idCity) throws Exception{
		if((idCity<0)||(idCity>super.getInstance().getNbCities()-1)) {
			throw new Exception("Error : index " + idCity
					+ " is not valid, it should range between 0 and "
					+ (super.getInstance().getNbCities()-1));
		}
		else {
			for(int i=0;i<idRestants.size();i++) {
				if(idRestants.get(i)==idCity) {
					idRestants.remove(i);
					break;
				}
			}
		}
	}
	//Renvoie la longueur totale parcourue (somme des trajets)
	public long getLongueur() throws Exception { //Max
		long distance=0;
		int villesuiv=this.getVillesParcourues().get(1);
		int villeprec=this.getVillesParcourues().get(0);
		int i=1;
		while (i<this.getVillesParcourues().size()) {
			distance=distance+super.getInstance().getDistances(villeprec, villesuiv);
			villeprec=villesuiv;
			villesuiv=this.getVillesParcourues().get(i+1);
			i++;
			
		}
		return distance;
	}
	
	//Renvoie la quantité supplémentaire de phéromones (delta tau) à déposer sur tous les arcs empruntés
	public double getDeltaPheromones() throws Exception { //Max
		long DeltaPheromones=0;
		DeltaPheromones=super.getQ()/this.getLongueur();
		return DeltaPheromones;
	}
	
	//utilise setPheromones pour modifier super.pheromones
	public void majPheromones() throws Exception { // Max
		double [][] MatPheromone=new double[this.getVillesParcourues().size()][this.getVillesParcourues().size()]; //le setPheromone([][] pheromones) requiert la creation du nouvelle matrice ph�ronome appel� MatPheromones
		for (int i=0;i<this.getVillesParcourues().size();i++) {
			for (int j=0;j<this.getVillesParcourues().size();i++) {
				super.setPheromones((super.getRho()*super.getPheromones(i,j))+this.getDeltaPheromones(),i,j);
				MatPheromone[i][j]=super.getRho()*super.getPheromones(i,j)+this.getDeltaPheromones();
			}
		}
		super.setPheromones(MatPheromone);
		
	}
	
	public ArrayList<Integer> getVillesRestantes() {
		return this.villesRestantes;
	}
	
	public ArrayList<Integer> getVillesParcourues() {
		return this.villesParcourues;
	}
}
