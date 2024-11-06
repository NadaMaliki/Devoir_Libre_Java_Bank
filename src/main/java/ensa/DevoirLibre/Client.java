package ensa.DevoirLibre;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Client {
	
	private int numclient;
	private String nom;
	private String prenom;
	private String addresse;
	private String phone;
	private String email;
	private List<Compte> comptes= new ArrayList<>();
	
		public Client () {
			super();
		}
		
		public Client(int num , String n , String p , String a , String e , String ph) {
			this.numclient=num;
			this.nom=n;
			this.prenom=p;
			this.addresse=a;
			this.email=e;
			this.phone=ph; 
		}
		
		public void AjoutClient(int numCL, String nom, String prenom, String addr, String email, String phone, List<Client> clients) {
		    try {
		        if (numCL <= 0) {
		            throw new IllegalArgumentException("Le numéro de client doit être positif.");
		        }
		        if (nom == null || nom.trim().isEmpty()) {
		            throw new IllegalArgumentException("Le nom ne peut pas être vide.");
		        }
		        if (prenom == null || prenom.trim().isEmpty()) {
		            throw new IllegalArgumentException("Le prénom ne peut pas être vide.");
		        }
		        if (addr == null || addr.trim().isEmpty()) {
		            throw new IllegalArgumentException("L'adresse ne peut pas être vide.");
		        }
		        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
		            throw new IllegalArgumentException("L'email ne peut pas être vide et doit contenir '@'.");
		        }
		        if (phone == null || phone.trim().isEmpty()) {
		            throw new IllegalArgumentException("Le numéro de téléphone ne peut pas être vide.");
		        }

		        Client client = new Client(numCL, nom, prenom, addr, email, phone);
		        clients.add(client);

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
	        Client client = (Client) obj;
	        return numclient == client.numclient; 
	    }
		
		@Override
	    public String toString() {
			if (comptes.isEmpty()) {
				return "Client Information:\n\n" +
	                   
	                   "NumClient: " + numclient + "\n" +
	                   "Nom: " + nom + "\n" +
	                   "Prénom: " + prenom + "\n" +
	                   "Adresse: " + addresse + "\n" +
	                   "Téléphone: " + phone + "\n" +
	                   "Email: " + email + "\n" +
	                   "Aucun compte associé. \n";
			} else {
				
				String result = "Client Information:\n\n" +
                        "NumClient: " + numclient + "\n" +
                        "Nom: " + nom + "\n" +
                        "Prénom: " + prenom + "\n" +
                        "Adresse: " + addresse + "\n" +
                        "Téléphone: " + phone + "\n" +
                        "Email: " + email + "\n" +
                        "Comptes associés :\n";

		        for (Compte compte : comptes) {
	                result += compte.toString() + "\n";

	
		        }


        		return result;
				
			}
	                   
	    }

	    
		public static void RechercheClient(List<Client> clients, Client clientRecherche) {
		    Client clientTrouve = null;
		    for (Client client : clients) {
		        if (client.equals(clientRecherche)) {  
		            clientTrouve = client;  
		            break;
		        }
		    }
		    
		    if (clientTrouve != null) {
		        System.out.println("Client trouvé :\n");
		        System.out.println(clientTrouve.toString());
		    } else {
		        System.out.println("Le client avec le numéro client " + clientRecherche.getNumclient() + " n'a pas été trouvé.");
		    }
		}
		
		public String toJson() {
	        Gson gson = new GsonBuilder().setPrettyPrinting().create();
	        return gson.toJson(this);
	    }

	    public static Client fromJson(String json) {
	        Gson gson = new Gson();
	        return gson.fromJson(json, Client.class);
	    }

		
}
