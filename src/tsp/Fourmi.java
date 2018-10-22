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
	public void choixVille() {
		
	}
	
	//Renvoie la longueur totale parcourue (somme des trajets)
	public int getLongueur() { //Max
		return 0;
	}
	
	//Renvoie la quantité supplémentaire de phéromones (delta tau) à déposer sur tous les arcs empruntés
	public double getDeltaPheromones() { //Max
		return 0.0;
	}
	
	//utilise setPheromones pour modifier super.pheromones
	public void majPheromones() { // Max
		// rho * ancienne valeur + delta tau
	}
	
	public ArrayList<Integer> getVillesRestantes() {
		return this.villesRestantes;
	}
	
	public ArrayList<Integer> getVillesParcourues() {
		return this.villesParcourues;
	}
}
