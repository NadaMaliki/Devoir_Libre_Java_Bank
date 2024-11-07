package ensa.DevoirLibre;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Getter
@Setter
public class Compte {
	private int numcompte;
	private Date datecrea;
	private Date dateupdate;
	private String device;
	private Client client;
	private Banque banque;
    @JsonManagedReference
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
	
	public Compte(int num, Date dc, Date du, String d, int clientId, int banqueId) {
		super();
		this.numcompte=num;
		this.datecrea=dc;
		this.dateupdate=du;
		this.device=d;
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
	        try {
	            ObjectMapper mapper = new ObjectMapper();
	            mapper.registerModule(new JavaTimeModule());
	            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
	        } catch (Exception e) {
	            System.out.println("Erreur lors de la conversion en JSON : " + e.getMessage());
	            return null;
	        }
	    }


	    public static Compte fromJson(String json) {
	        Gson gson = new Gson();
	        return gson.fromJson(json, Compte.class);
	    }
	    
	    
	    //Avec la Base de données
	    public void CreationCompte_BD(Date creation, Date update, String device, Client client, Banque banque) {
	        String sql = "INSERT INTO compte ( datecrea, dateupdate, device, numclient, banque_id) VALUES (?, ?, ?, ?, ?)";

	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

	            stmt.setDate(1, new java.sql.Date(creation.getTime()));  
	            stmt.setDate(2, new java.sql.Date(update.getTime()));   
	            stmt.setString(3, device);
	            stmt.setInt(4, client.getNumclient());  
	            stmt.setInt(5, banque.getId());  

	            stmt.executeUpdate();

	            ResultSet generatedKeys = stmt.getGeneratedKeys();
	            if (generatedKeys.next()) {
	                int generatedNumCompte = generatedKeys.getInt(1);  
	                System.out.println("Compte ajouté avec succès. NumCompte généré: " + generatedNumCompte);
	            }

	        } catch (SQLException e) {
	            System.out.println("Erreur lors de l'ajout du compte : " + e.getMessage());
	        }
	    }

	    
	    public static void RechercheCompte_BD(int numcompteRecherche) {
	        String sql = "SELECT * FROM compte WHERE numcompte = ?";

	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {

	            stmt.setInt(1, numcompteRecherche);
	            ResultSet rs = stmt.executeQuery();

	            if (rs.next()) {
	                System.out.println("Résultat trouvé.");

	                int numcompte = rs.getInt("numcompte");
	             	Date datecrea = rs.getDate("datecrea");
	                Date dateupdate = rs.getDate("dateupdate");
	                String device = rs.getString("device");
	                int clientId = rs.getInt("numclient");
	                int banqueId = rs.getInt("banque_id");


	                Compte compteTrouve = new Compte(numcompte, datecrea, dateupdate, device, clientId, banqueId);
	                System.out.println("Compte trouvé :\n");
	                System.out.println(compteTrouve.toString());
	            } else {
	                System.out.println("Le compte avec le numéro " + numcompteRecherche + " n'a pas été trouvé.");
	            }

	        } catch (SQLException e) {
	            System.out.println("Erreur lors de la recherche du compte : " + e.getMessage());
	        }
	    }

}
