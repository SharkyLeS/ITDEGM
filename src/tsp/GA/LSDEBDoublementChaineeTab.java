package tsp.GA; 	  	 	 	  			 	 	 	
 	  	 	 	  			 	 	 	
public class LSDEBDoublementChaineeTab { 	  	 	 	  			 	 	 	
 	  	 	 	  			 	 	 	
	public static final int NULL = -1; 	  	 	 	  			 	 	 	
 	  	 	 	  			 	 	 	
	private boolean[] estPresent; 	  	 	 	  			 	 	 	
	// pour tout i de l'intervalle [0, BORNE[, 	  	 	 	  			 	 	 	
	// estPresent[ i ]==true si l'entier i est present. 	  	 	 	  			 	 	 	
 	  	 	 	  			 	 	 	
	private int[] predecesseur; 	  	 	 	  			 	 	 	
	// pour tout i de l'intervalle [0, BORNE[, 	  	 	 	  			 	 	 	
	// predecesseur[ i ] vaut NULL si l'entier i est 	  	 	 	  			 	 	 	
	// absent de la liste  OU  si i est present 	  	 	 	  			 	 	 	
	// mais n'a pas de predecesseur (i est en 	  	 	 	  			 	 	 	
	// tete de liste). predecesseur[ i ] vaut j si j est le 	  	 	 	  			 	 	 	
	// predecesseur de i dans la liste. 	  	 	 	  			 	 	 	
 	  	 	 	  			 	 	 	
	private int[] successeur; 	  	 	 	  			 	 	 	
	// pour tout i de l'intervalle [0, BORNE[, 	  	 	 	  			 	 	 	
	// successeur[ i ] vaut NULL si l'entier i est absent 	  	 	 	  			 	 	 	
	// OU si i est present mais n'a pas de successeur (i est en 	  	 	 	  			 	 	 	
	// queue de liste). successeur[ i ] vaut j si j est le 	  	 	 	  			 	 	 	
	// successeur de i dans la liste. 	  	 	 	  			 	 	 	
 	  	 	 	  			 	 	 	
	public int[] getPredecesseur() {
		return predecesseur;
	}
	public int[] getSuccesseur() {
		return successeur;
	}
	public int getTete() {
		return tete;
	}
	public int getQueue() {
		return queue;
	}
	private int tete; 	  	 	 	  			 	 	 	
	// L'entier qui est en tete de liste si la liste n'est pas vide 	  	 	 	  			 	 	 	
	// (vaut NULL si la liste est vide) 	  	 	 	  			 	 	 	
 	  	 	 	  			 	 	 	
	private int queue; 	  	 	 	  			 	 	 	
	// L'entier qui est en queue de liste si la liste n'est pas vide 	  	 	 	  			 	 	 	
	// (vaut NULL si la liste est vide) 	  	 	 	  			 	 	 	
 	  	 	 	  			 	 	 	
	/** 	  	 	 	  			 	 	 	
	 * Constructeur initialisant la liste a vide 	  	 	 	  			 	 	 	
	 * (avec borne pour borne des entiers qu'elle peut contenir) 	  	 	 	  			 	 	 	
	 * @param borne la borne des entiers que la liste peut contenir 	  	 	 	  			 	 	 	
	 */ 	  	 	 	  			 	 	 	
	public LSDEBDoublementChaineeTab(int borne) { 	  	 	 	  			 	 	 	
		this.estPresent = new boolean[borne]; 	  	 	 	  			 	 	 	
		this.predecesseur = new int[borne]; 	  	 	 	  			 	 	 	
		this.successeur = new int[borne]; 	  	 	 	  			 	 	 	
		this.tete = NULL; 	  	 	 	  			 	 	 	
		this.queue = NULL; 	  	 	 	  			 	 	 	
		for (int i=0; i<borne; i++) { 	  	 	 	  			 	 	 	
			this.estPresent[i]=false; 	  	 	 	  			 	 	 	
			this.predecesseur[i]=NULL; 	  	 	 	  			 	 	 	
			this.successeur[i]=NULL; 	  	 	 	  			 	 	 	
		} 	  	 	 	  			 	 	 	
	} 	  	 	 	  			 	 	 	
	/** 	  	 	 	  			 	 	 	
	 * Ce constructeur n'est utilise que pour les tests 	  	 	 	  			 	 	 	
	 */ 	  	 	 	  			 	 	 	
	public LSDEBDoublementChaineeTab(boolean[] estPresent, int[] predecesseur, int[] successeur, int tete, int queue) { 	  	 	 	  			 	 	 	
		this.estPresent = estPresent; 	  	 	 	  			 	 	 	
		this.predecesseur = predecesseur; 	  	 	 	  			 	 	 	
		this.successeur = successeur; 	  	 	 	  			 	 	 	
		this.tete = tete ; 	  	 	 	  			 	 	 	
		this.queue = queue; 	  	 	 	  			 	 	 	
	} 	  	 	 	  			 	 	 	
 	  	 	 	  			 	 	 	
 	  	 	 	  			 	 	 	
	/** Retourne la borne */ 	  	 	 	  			 	 	 	
	public int getBorne() { 	  	 	 	  			 	 	 	
		return this.estPresent.length; 	  	 	 	  			 	 	 	
	} 	  	 	 	  			 	 	 	
 	  	 	 	  			 	 	 	
	/** Retourne true si la liste est vide (false sinon) */ 	  	 	 	  			 	 	 	
	public boolean estVide() { 	  	 	 	  			 	 	 	
		return this.tete==NULL; 	  	 	 	  			 	 	 	
	} 	  	 	 	  			 	 	 	
 	  	 	 	  			 	 	 	
	/** Retourne true si la liste est un singleton, 	  	 	 	  			 	 	 	
	 * c'est a dire si elle contient exactement UN element */ 	  	 	 	  			 	 	 	
	public boolean estSingleton() { 	  	 	 	  			 	 	 	
		return this.tete!=NULL && this.queue==this.tete; 	  	 	 	  			 	 	 	
		// ou 	  	 	 	  			 	 	 	
		// return this.tete!=NULL && this.predecesseur[this.tete]==NULL && this.successeur[this.tete]=null; 	  	 	 	  			 	 	 	
	} 	  	 	 	  			 	 	 	
 	  	 	 	  			 	 	 	
	/** Retourne true si la valeur val figure dans la liste (retourne false sinon) */ 	  	 	 	  			 	 	 	
	public boolean contains( int val) { 	  	 	 	  			 	 	   	 	 	  			 	 	 	 	  	 	 	  			 	 	 	
		return estPresent[val]; 	  	 	 	  			 	 	 	
	} 	  	 	 	  			 	 	 	
 	  	 	 	  			 	 	 	
	/** si val est dans [0, this.getBorne()[ et la liste ne contient pas la valeur val, 	  	 	 	  			 	 	 	
	 *  ajoute la valeur val en tete de la liste et retourne true. 	  	 	 	  			 	 	 	
	 *  Sinon, ne modifie en rien la liste et retourne false. 	  	 	 	  			 	 	 	
	 */ 	  	 	 	  			 	 	 	
	public boolean ajouterTete( int val) { 	  	 	 	  			 	 	 	
		if((val>=0)&&(val<this.getBorne())&&(!this.contains(val))){ 	  	 	 	  			 	 	 	
			if((this.tete==this.queue)&&(this.tete==NULL)) { 	  	 	 	  			 	 	 	
				this.queue=val; 	  	 	 	  			 	 	 	
			} 	  	 	 	  			 	 	 	
			else { 	  	 	 	  			 	 	 	
				int ancienneTete = this.tete; 	  	 	 	  			 	 	 	
				this.predecesseur[ancienneTete]=val; 	  	 	 	  			 	 	 	
				this.successeur[val]=ancienneTete; 	  	 	 	  			 	 	 	
			} 	  	 	 	  			 	 	 	
			this.tete=val; 	  	 	 	  			 	 	 	
			this.estPresent[val]=true; 	  	 	 	  			 	 	 	
			return true; 	  	 	 	  			 	 	 	
		} 	  	 	 	  			 	 	 	
		return false; 	  	 	 	  			 	 	 	
	} 	  	 	 	  			 	 	 	
 	  	 	 	  			 	 	 	
	/** si val est dans [0, this.getBorne()[ et la liste ne contient pas la valeur val, 	  	 	 	  			 	 	 	
	 *  ajoute la valeur val en queue de la liste et retourne true. 	  	 	 	  			 	 	 	
	 *  Sinon, ne modifie en rien la liste et retourne false. 	  	 	 	  			 	 	 	
	 */ 	  	 	 	  			 	 	 	
	public boolean ajouterQueue( int val) { 	  	 	 	  			 	 	 	
		if((val>=0)&&(val<this.getBorne())&&(!this.contains(val))){ 	  	 	 	  			 	 	 	
			if((this.tete==this.queue)&&(this.tete==NULL)) { 	  	 	 	  			 	 	 	
				this.tete=val; 	  	 	 	  			 	 	 	
			} 	  	 	 	  			 	 	 	
			else { 	  	 	 	  			 	 	 	
				int ancienneQueue = this.queue; 	  	 	 	  			 	 	 	
				this.successeur[ancienneQueue]=val; 	  	 	 	  			 	 	 	
				this.predecesseur[val]=ancienneQueue; 	  	 	 	  			 	 	 	
			} 	  	 	 	  			 	 	 	
			this.queue=val; 	  	 	 	  			 	 	 	
			this.estPresent[val]=true; 	  	 	 	  			 	 	 	
			return true; 	  	 	 	  			 	 	 	
		} 	  	 	 	  			 	 	 	
		return false; 	  	 	  	  	 	 	  			 	 	 		 	 	  			 	 	 	
	} 	  	 	 	  			 	 	 	
 	  	 	 	  			 	 	 	
	/** Leve une erreur (throw new Error("Retrait sur une liste vide");) 	  	 	 	  			 	 	 	
	 * si la liste est vide. Sinon, supprime de la liste la valeur en tete et 	  	 	 	  			 	 	 	
	 * retourne cette valeur. 	  	 	 	  			 	 	 	
	 */ 	  	 	 	  			 	 	 	
	public int retirerTete() { 	  	 	 	  			 	 	 	
		if((this.tete==this.queue)&&(this.tete==NULL)) throw new Error("Retrait sur une liste vide"); 	  	 	 	  			 	 	 	
		else { 	  	 	 	  			 	 	 	
			int ancienneTete = this.tete; 	  	 	 	  			 	 	 	
			if(estSingleton()) { 	  	 	 	  			 	 	 	
				this.estPresent[ancienneTete]=false; 	  	 	 	  			 	 	 	
				this.tete=NULL; 	  	 	 	  			 	 	 	
				this.queue=NULL; 	  	 	 	  			 	 	 	
			} 	  	 	 	  			 	 	 	
			else { 	  	 	 	  			 	 	 	
				this.tete=this.successeur[ancienneTete]; 	  	 	 	  			 	 	 	
				this.estPresent[ancienneTete]=false; 	  	 	 	  			 	 	 	
				this.predecesseur[this.tete]=NULL; 	  	 	 	  			 	 	 	
				this.successeur[ancienneTete]=NULL; 	  	 	 	  			 	 	 	
			} 	  	 	 	  			 	 	 	
			return ancienneTete; 	  	 	 	  			 	 	 	
		} 	  	 	 	  			 	 	 	
	} 	  	 	 	  			 	 	 	
 	  	 	 	  			 	 	 	
	/** Leve une erreur (throw new Error("Retrait sur une liste vide");) 	  	 	 	  			 	 	 	
	 * si la liste est vide. Sinon, supprime de la liste la valeur en queue et 	  	 	 	  			 	 	 	
	 * retourne cette valeur. 	  	 	 	  			 	 	 	
	 */ 	  	 	 	  			 	 	 	
	public int retirerQueue(){ 	  	 	 	  			 	 	 	
		if((this.tete==this.queue)&&(this.tete==NULL)) throw new Error("Retrait sur une liste vide"); 	  	 	 	  			 	 	 	
		else { 	  	 	 	  			 	 	 	
			int ancienneQueue = this.queue; 	  	 	 	  			 	 	 	
			if(estSingleton()) { 	  	 	 	  			 	 	 	
				this.estPresent[ancienneQueue]=false; 	  	 	 	  			 	 	 	
				this.tete=NULL; 	  	 	 	  			 	 	 	
				this.queue=NULL; 	  	 	 	  			 	 	 	
			} 	  	 	 	  			 	 	 	
			else { 	  	 	 	  			 	 	 	
				this.queue=this.predecesseur[ancienneQueue]; 	  	 	 	  			 	 	 	
				this.estPresent[ancienneQueue]=false; 	  	 	 	  			 	 	 	
				this.successeur[this.queue]=NULL; 	  	 	 	  			 	 	 	
				this.predecesseur[ancienneQueue]=NULL; 	  	 	 	  			 	 	 	
			} 	  	 	 	  			 	 	 	
			return ancienneQueue; 	  	 	 	  			 	 	 	
		} 	  	 	 	  		 	  	 	 	  			 	 	 	  	 	 	  			 	 	 	
	} 	  	 	 	  			 	 	 	
	/** Si la liste contient la valeur val, retire cette valeur de la liste et retourne true. 	  	 	 	  			 	 	 	
	 * Sinon, ne modifie en rien la liste et retourne false. 	  	 	 	  			 	 	 	
	 */ 	  	 	 	  			 	 	 	
	public boolean retirer(int val) { 	  	 	 	  			 	 	 	
		if(this.contains(val)) { 	  	 	 	  			 	 	 	
			if(this.tete==val) retirerTete(); 	 	  	 	 	  			 	 	 	
			else { 	  	 	 	  			 	 	 	
				if(this.queue==val) retirerQueue(); 	  	 	 	  			 	 	 	
				else { 	  	 	 	  			 	 	 	
					this.estPresent[val]=false; 	  	 	 	  			 	 	 	
					int predecesseur = this.predecesseur[val]; 	  	 	 	  			 	 	 	
					int successeur = this.successeur[val]; 	  	 	 	  			 	 	 	
					this.successeur[predecesseur]=successeur; 	  	 	 	  			 	 	 	
					this.predecesseur[successeur]=predecesseur;  	  	 	 	  			 	 	 	
					this.successeur[val]=NULL; 	  	 	 	  			 	 	 	
					this.predecesseur[val]=NULL; 	  	 	 	  			 	 	 	
				} 	  	 	 	  			 	 	 		  	 	 	  			 	 	 	
			} 	   	  	 	 	  			 	 	 	
			return true;  	  	 	 	  			 	 	 	
		} 	  	 	 	  			 	 	 	
		else return false; 	  	 	 	  			 	 	 	
	} 	  	 	 	  			 	 	 	
 	  	 	 	  			 	 	 	
 	  	 	 	  			 	 	 	
	/** Retourne le nombre d'elements presents dans la liste */ 	  	 	 	  			 	 	 	
	public int size() { 	  	 	 	  			 	 	 	
		int size = 0; 	  	 	 	  			 	 	 	
		for(int i=0;i<getBorne();i++) { 	  	 	 	  			 	 	 	
			if(this.contains(i)) size++; 	  	 	 	  			 	 	 	
		} 	  	 	 	  			 	 	 	
		return size; 	  	 	 	  			 	 	 	
	} 	  	 	 	  			 	 	 	
 	  	 	 	  			 	 	 	
 	  	 	 	  			 	 	 	
	public static String sur6(int i){ 	  	 	 	  			 	 	 	
		String s=""+i; 	  	 	 	  			 	 	 	
		while (s.length()<6) { 	  	 	 	  			 	 	 	
			s=" "+s; 	  	 	 	  			 	 	 	
		} 	  	 	 	  			 	 	 	
		return s; 	  	 	 	  			 	 	 	
	} 	  	 	 	  			 	 	 	
	/** Retourne une chaine de caracteres formee des valeurs quelle contient. 	  	 	 	  			 	 	 	
	 * Ex. : - si la liste est vide, alors toString() retourne "{}" 	  	 	 	  			 	 	 	
	 *       - si la liste ne comporte que la valeur 6, alors toString() retourne "{6}" 	  	 	 	  			 	 	 	
	 *       - si la liste est composee des valeurs 4, 2 et 8 (dans cet ordre) 	  	 	 	  			 	 	 	
	 *         alors toString() retourne la chaine "{4, 2, 8}" 	  	 	 	  			 	 	 	
	 */ 	  	 	 	  			 	 	 	
	public String toString() { 	  	 	 	  			 	 	 	
		String pres = " pres:"; 	  	 	 	  			 	 	 	
		String prec = " prec:"; 	  	 	 	  			 	 	 	
		String succ = " succ:"; 	  	 	 	  			 	 	 	
		String tete, queue; 	  	 	 	  			 	 	 	
 	  	 	 	  			 	 	 	
		for (int i =0; i<this.getBorne(); i++) { 	  	 	 	  			 	 	 	
			if (this.estPresent[i]==true) { 	  	 	 	  			 	 	 	
				pres+="  TRUE"; 	  	 	 	  			 	 	 	
			} else { 	  	 	 	  			 	 	 	
				pres+=" FALSE"; 	  	 	 	  			 	 	 	
			} 	  	 	 	  			 	 	 	
			if (this.predecesseur[i]==NULL) { 	  	 	 	  			 	 	 	
				prec+="  NULL"; 	  	 	 	  			 	 	 	
			} else { 	  	 	 	  			 	 	 	
				prec+=sur6(this.predecesseur[i]); 	  	 	 	  			 	 	 	
			} 	  	 	 	  			 	 	 	
			if (this.successeur[i]==NULL) { 	  	 	 	  			 	 	 	
				succ+="  NULL"; 	  	 	 	  			 	 	 	
			} else { 	  	 	 	  			 	 	 	
				succ+=sur6(this.successeur[i]); 	  	 	 	  			 	 	 	
			} 	  	 	 	  			 	 	 	
		} 	  	 	 	  			 	 	 	
		if (this.tete==NULL) { 	  	 	 	  			 	 	 	
			tete="  NULL"; 	  	 	 	  			 	 	 	
		} else { 	  	 	 	  			 	 	 	
			tete=sur6(this.tete); 	  	 	 	  			 	 	 	
		} 	  	 	 	  			 	 	 	
		if (this.queue==NULL) { 	  	 	 	  			 	 	 	
			queue="  NULL"; 	  	 	 	  			 	 	 	
		} else { 	  	 	 	  			 	 	 	
			queue=sur6(this.queue); 	  	 	 	  			 	 	 	
		} 	  	 	 	  			 	 	 	
		return pres+"\n"+prec+"\n"+succ+"\n"+" tete:"+tete+"\n"+"queue:"+queue; 	  	 	 	  			 	 	 	
	} 	  	 	 	  			 	 	 	
} 	  	 	 	  			 	 	 	
