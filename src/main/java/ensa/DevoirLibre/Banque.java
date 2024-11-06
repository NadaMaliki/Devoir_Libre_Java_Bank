package ensa.DevoirLibre;

import java.util.*;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter

public class Banque {
	private int id;
	private String pays;
	private List<Compte> comptes;
	
	public Banque () {
		super();
	}
	
	
	public Banque (int id, String p) {
		this.id=id;
		this.pays=p;
		this.comptes = new ArrayList<>(); 
	}
	
	@Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;  
        }
        if (obj == null) {
            return false; 
        }
        Banque banque = (Banque) obj;
        return id == banque.id; 
    }
	
	public boolean paysDifferent(Banque autreBanque) {
	    if (!this.pays.equals(autreBanque.getPays())) {
	        return true; 
	    } else {
	        return false; 
	    }
	}

}
