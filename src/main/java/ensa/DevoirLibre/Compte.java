package ensa.DevoirLibre;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Compte {
	private int numcompte;
	private Date datecrea;
	private Date dateupdate;
	private String device;
	private Client client;
	private Banque banque;
	private List<Transaction> transactions= new ArrayList<>() ;
	
	public Compte () {
		super();
	}
	
	
	public Compte(int num , Date dc , Date du , String d, Client c, Banque b) {
		this.numcompte=num;
		this.datecrea=dc;
		this.dateupdate=du;
		this.device=d;
		this.client=c;
		this.banque=b;
	}
	
	public void CreationCompte(int numC , Date creation , Date update , String device, Client client, Banque banque, List<Compte> comptes) {
		try {
			if (numC <= 0) {
		        throw new IllegalArgumentException("Le numéro de compte doit être positif.");
		    }
		    if (creation == null ) {
		        throw new IllegalArgumentException("La date de création ne peut pas être nulle.");
		    }
		    if (update == null) {
		        throw new IllegalArgumentException("La date de mise à jour ne peut pas être nulle.");
		    }
		    if (device == null ) {
		        throw new IllegalArgumentException("Le dispositif ne peut pas être vide.");
		    }
		    if (client == null) {
		        throw new IllegalArgumentException("Le client ne peut pas être nul.");
		    }
		    if (banque == null) {
		        throw new IllegalArgumentException("La banque ne peut pas être nulle.");
		    }
		    
			Compte compte=new Compte(numC, creation, update,  device, client, banque);
			client.getComptes().add(compte);
			banque.getComptes().add(compte);
			
			comptes.add(compte);
		} catch (IllegalArgumentException e) {
	        System.out.println("Erreur : " + e.getMessage());
	    }
	}

	   @Override
	    public boolean equals(Object obj) {
	        if (this == obj) {
	            return true;
	        }
	        if (obj == null) {
	            return false;
	        }
	        Compte compte = (Compte) obj;
	        return numcompte == compte.numcompte; 
	    }

	    @Override
	    public String toString() {
	        String result = "Compte Information:\n" +
	                        "NumCompte: " + numcompte + "\n" +
	                        "Date de création: " + datecrea + "\n" +
	                        "Dernière mise à jour: " + dateupdate + "\n" +
	                        "Device: " + device + "\n";
	        
	        if (transactions.isEmpty()) {
	            result += "Aucune transaction associée.\n";
	        } else {
	            result += "Transactions associées:\n";
	            for (Transaction transaction : transactions) {
	                result += transaction.toString() + "\n";
	            }
	        }

	        return result;
	    }
	    
	    public static void RechercheCompte(List<Compte> comptes, Compte compteRecherche) {
	    	Compte CompteTrouve = null;
		    for (Compte compte : comptes) {
		        if (compte.equals(compteRecherche)) {  
		        	CompteTrouve = compte;  
		            break;
		        }
		    }
		    
		    if (CompteTrouve != null) {
		        System.out.println("Compte trouvé :\n");
		        System.out.println(CompteTrouve.toString());
		    } else {
		        System.out.println("Le Compte avec le numéro " + compteRecherche.getNumcompte() + " n'a pas été trouvé.");
		    }
		}
	    
	    public String toJson() {
	        Gson gson = new GsonBuilder().setPrettyPrinting().create();
	        return gson.toJson(this);
	    }

	    public static Compte fromJson(String json) {
	        Gson gson = new Gson();
	        return gson.fromJson(json, Compte.class);
	    }
	
}
