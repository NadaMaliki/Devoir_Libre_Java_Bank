package ensa.DevoirLibre;

import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;


@Getter

public final class Transaction {
	private final Transtype type;
	private final LocalDateTime timestemp;
	private final String reference;
	private final Compte compte_src;
	private final List<Compte> comptes_dst;

	
	public Transaction(Transtype type , LocalDateTime temps , String r, Compte cs, List<Compte> comptes_dst) {
		// si transaction entre deux comptes seulement
		if(comptes_dst.size()==1) {
			//si c'est dans le meme pays
			if (!cs.getBanque().paysDifferent(comptes_dst.get(0).getBanque())){
				// si la meme banque et meme pays
				if (cs.getBanque().equals(comptes_dst.get(0).getBanque())) {
					this.type = Transtype.VIRINT;  
				}
				// si la  deux differentes banques et meme pays
				else{
					this.type = Transtype.VIREST;
				}
		    } 
			// si c'est deux pays differents et deux banques differentes
			else {
		        this.type = Transtype.VIRCHAC;  
		    }
		}
		// si entre plusieurs comptes
		else {
			this.type = Transtype.VIRTMULTA;
		}
		
		this.timestemp=temps;
		this.reference=r;
		this.compte_src=cs;
		this.comptes_dst=new ArrayList<>(comptes_dst);
	}

	public void CreationTransaction(Transtype type , LocalDateTime date , String ref, Compte compte_src, List<Compte> comptes_dst, List<Transaction> transactions) {
		try {
			if (type == null) {
		        throw new IllegalArgumentException("Le type de la transaction ne peut pas être nul.");
		    }
		    if (date == null) {
		        throw new IllegalArgumentException("La date de la transaction ne peut pas être nulle.");
		    }
		    if (ref == null || ref.trim().isEmpty()) {
		        throw new IllegalArgumentException("La référence de la transaction ne peut pas être vide.");
		    }
		    if (compte_src == null) {
		        throw new IllegalArgumentException("Le compte source ne peut pas être nul.");
		    }
		    if (comptes_dst == null || comptes_dst.isEmpty()) {
		        throw new IllegalArgumentException("La liste des comptes de destination ne peut pas être vide.");
		    }
	
			Transaction transaction= new Transaction(type, date, ref, compte_src, comptes_dst);
			compte_src.getTransactions().add(transaction);
			for (Compte compte_dst : comptes_dst) {
					compte_dst.getTransactions().add(transaction);
			}
			transactions.add(transaction);
		
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
	        Transaction transaction = (Transaction) obj;
	        return reference.equals(transaction.reference); 
	    }

	    @Override
	    public String toString() {
	        String result = "Transaction Information:\n" +
	                        "Type: " + type + "\n" +
	                        "Timestamp: " + timestemp + "\n" +
	                        "Reference: " + reference + "\n" +
	                        "Compte Source: " + compte_src.getNumcompte() + "\n" +
	                        "Compte Destination(s): \n";

	            for (Compte compte : comptes_dst) {
	                result += "  - " + compte.getNumcompte() + "\n";
	            }

	        return result;
	    }
	    
	    public static void RechercheTransaction(List<Transaction> transactions, Transaction transactionRecherche) {
	    	Transaction transactionTrouve = null;
		    for (Transaction transaction : transactions) {
		        if (transaction.equals(transactionRecherche)) {  
		        	transactionTrouve = transaction;  
		            break;
		        }
		    }
		    
		    if (transactionTrouve != null) {
		        System.out.println("transaction trouvé :\n");
		        System.out.println(transactionTrouve.toString());
		    } else {
		        System.out.println("Le transaction avec le numéro " + transactionRecherche.getReference() + " n'a pas été trouvé.");
		    }
		}
	    
	    public String toJson() {
	        Gson gson = new GsonBuilder().setPrettyPrinting().create();
	        return gson.toJson(this);
	    }

	    public static Transaction fromJson(String json) {
	        Gson gson = new Gson();
	        return gson.fromJson(json, Transaction.class);
	    }
	
}
