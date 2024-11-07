package ensa.DevoirLibre;

import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

import com.google.gson.Gson;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


@Getter

public final class Transaction {
	private final Transtype type;
	private final LocalDateTime timestemp;
	private final String reference;
	private final Compte compte_src;
    @JsonManagedReference
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
	        try {
	            ObjectMapper mapper = new ObjectMapper();
	            mapper.registerModule(new JavaTimeModule());
	            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
	        } catch (Exception e) {
	            System.out.println("Erreur lors de la conversion en JSON : " + e.getMessage());
	            return null;
	        }
	    }


	    public static Transaction fromJson(String json) {
	        Gson gson = new Gson();
	        return gson.fromJson(json, Transaction.class);
	    }
	    
	    
	    //Avec la base de données
	    private static Transtype determineType(Compte cs, List<Compte> comptes_dst) {
	        if (comptes_dst.size() == 1) {
	            if (!cs.getBanque().paysDifferent(comptes_dst.get(0).getBanque())) {
	                return cs.getBanque().equals(comptes_dst.get(0).getBanque()) ? Transtype.VIRINT : Transtype.VIREST;
	            } else {
	                return Transtype.VIRCHAC;
	            }
	        } else {
	            return Transtype.VIRTMULTA;
	        }
	    }

	    public void CreerTransaction_BD(LocalDateTime date, String ref, Compte compte_src, List<Compte> comptes_dst) {
	        Transtype autoType = determineType(compte_src, comptes_dst);  

	        String sql = "INSERT INTO Transaction (type, timestamp, reference, compte_src_id) VALUES (?, ?, ?, ?)";
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
	            
	            stmt.setString(1, autoType.name());
	            stmt.setObject(2, date);
	            stmt.setString(3, ref);
	            stmt.setInt(4, compte_src.getNumcompte());

	            int affectedRows = stmt.executeUpdate();

	            if (affectedRows > 0) {
	                ResultSet generatedKeys = stmt.getGeneratedKeys();
	                if (generatedKeys.next()) {
	                    int transactionId = generatedKeys.getInt(1);
	                    
	                    for (Compte compte_dst : comptes_dst) {
	                        String linkSql = "INSERT INTO Transaction_Compte (transaction_id, compte_id) VALUES (?, ?)";
	                        try (PreparedStatement linkStmt = conn.prepareStatement(linkSql)) {
	                            linkStmt.setInt(1, transactionId);
	                            linkStmt.setInt(2, compte_dst.getNumcompte());
	                            linkStmt.executeUpdate();
	                        }
	                    }
	                }
	            }
	        } catch (SQLException e) {
	            System.out.println("Erreur lors de la création de la transaction : " + e.getMessage());
	        }
	    }
	    
	    public static void RechercheTransaction_BD(String reference) {
	        String sql = "SELECT * FROM Transaction WHERE reference = ?";
	        
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(sql)) {
	            
	            stmt.setString(1, reference);
	            ResultSet rs = stmt.executeQuery();
	            
	            if (rs.next()) {
	                int id = rs.getInt("id");
	                String type = rs.getString("type");
	                LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();
	                String ref = rs.getString("reference");
	                int compteSrcId = rs.getInt("compte_src_id");
	                
	                
	                
	                Transtype transt = Transtype.valueOf(type);
	                Transaction transaction = new Transaction(transt, timestamp, ref, null, null);
	                
	                System.out.println("Transaction trouvée : ");
	                System.out.println("Référence : " + transaction.getReference());
	                System.out.println("Type : " + transaction.getType());
	                System.out.println("Timestamp : " + transaction.getTimestemp());
	                System.out.println("Compte source ID : " + compteSrcId); 
	                
	                String dstSql = "SELECT * FROM Transaction_Compte WHERE transaction_id = ?";
	                try (PreparedStatement dstStmt = conn.prepareStatement(dstSql)) {
	                    dstStmt.setInt(1, id);
	                    ResultSet dstRs = dstStmt.executeQuery();
	                    System.out.println("Comptes de destination : ");
	                    while (dstRs.next()) {
	                        int compteDstId = dstRs.getInt("compte_id");
	                        System.out.println("- Compte de destination ID : " + compteDstId);  // Afficher uniquement l'ID
	                    }
	                }
	                
	            } else {
	                System.out.println("Transaction avec la référence " + reference + " non trouvée.");
	            }
	        } catch (SQLException e) {
	            System.out.println("Erreur lors de la recherche de la transaction : " + e.getMessage());
	        }
	    }



	
}
