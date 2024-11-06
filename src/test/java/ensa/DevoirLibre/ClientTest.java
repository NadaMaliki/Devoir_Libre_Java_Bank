package ensa.DevoirLibre;

import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class ClientTest {

	private List<Client> clients;
    private Client clientExistant;
    private Client clientInexistant;

    @BeforeEach
    public void setUp() {
        clients = new ArrayList<>();
        
        // Initialisation des clients pour les tests
        clientExistant = new Client(1, "John", "Doe", "123 St", "john@example.com", "123456789");
        clientInexistant = new Client(2, "Jane", "Smith", "456 St", "jane@example.com", "987654321");
        
        // Ajout du client existant à la liste
        clients.add(clientExistant);
    }
    
    
    @Test
    public void testAjoutClientValide() {
        List<Client> clients = new ArrayList<>();
        Client client = new Client();

        // Ajout d'un client valide
        client.AjoutClient(1, "John", "Doe", "123 Rue Exemple", "john.doe@example.com", "0123456789", clients);

        // Vérification que la taille de la liste a bien augmenté de 1
        assertEquals(1, clients.size(), "La taille de la liste après ajout doit être 1");
        
        // Vérification des informations du client ajouté
        Client clientAjoute = clients.get(0);
        assertEquals(1, clientAjoute.getNumclient(), "Le numéro du client ajouté doit être 1");
        assertEquals("Doe", clientAjoute.getPrenom(), "Le prenom du client ajouté doit être John");
        assertEquals("John", clientAjoute.getNom(), "Le nom du client ajouté doit être Doe");
        assertEquals("123 Rue Exemple", clientAjoute.getAddresse(), "L'adresse du client ajouté doit être '123 Rue Exemple'");
        assertEquals("john.doe@example.com", clientAjoute.getEmail(), "L'email du client ajouté doit être 'john.doe@example.com'");
        assertEquals("0123456789", clientAjoute.getPhone(), "Le numéro de téléphone du client ajouté doit être '0123456789'");
        
        System.out.println("Le client: " + clients.get(0).getPrenom() + " " + clients.get(0).getNom()+ "\t addresse: " + clients.get(0).getAddresse() + "\t email: " + clients.get(0).getEmail() + "\t Phone: " +clients.get(0).getPhone() + "\t est bien ajouté."  );

    }

    @Test
    public void testAjoutClientNumeroClientInvalide() {
        List<Client> clients = new ArrayList<>();
        Client client = new Client();

        // Test avec un numéro de client invalide (<= 0)
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        	client.AjoutClient(0, "John", "Doe", "123 Rue Exemple", "john.doe@example.com", "0123456789", clients);
        });
        assertEquals("Le numéro de client doit être positif.", exception.getMessage(), "Le message d'exception pour numéro de client invalide doit être correct");
    }

    @Test
    public void testAjoutClientNomVide() {
        List<Client> clients = new ArrayList<>();
        Client client = new Client();

        // Test avec un nom vide
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        	client.AjoutClient(1, "", "Doe", "123 Rue Exemple", "john.doe@example.com", "0123456789", clients);
        });
        assertEquals("Le nom ne peut pas être vide.", exception.getMessage(), "Le message d'exception pour un nom vide doit être correct");
    }

    @Test
    public void testAjoutClientEmailInvalide() {
        List<Client> clients = new ArrayList<>();
        Client client = new Client();

        // Test avec un email invalide (sans @)
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        	client.AjoutClient(1, "John", "Doe", "123 Rue Exemple", "john.doeexample.com", "0123456789", clients);
        });
        assertEquals("L'email ne peut pas être vide et doit contenir '@'.", exception.getMessage(), "Le message d'exception pour un email invalide doit être correct");
    }
        
    
    @Test
    public void testRechercheClient_ClientExistant() {
        // Recherche d'un client existant 
        Client.RechercheClient(clients, clientExistant);  
          
    }

    @Test
    public void testRechercheClient_ClientInexistant() {
        // Recherche d'un client inexistant en utilisant la méthode RechercheClient
        Client.RechercheClient(clients, clientInexistant); 
    }
    
    @Test
    public void testClientToJson() {
        Client client = new Client(1, "John", "Doe", "123 St", "john@example.com", "123456789");
        String clientJson = client.toJson();
        System.out.println("Client en JSON : " + clientJson);
        assertNotNull(clientJson, "La conversion en JSON ne doit pas renvoyer null");

        // Conversion inverse pour vérifier la cohérence
        Client clientFromJson = Client.fromJson(clientJson);
        System.out.println("Client reconstruite depuis JSON : " + clientJson.toString());
        assertEquals(client.getNumclient(), clientFromJson.getNumclient(), "Les numéros de client doivent être identiques après conversion");
        assertEquals(client.getNom(), clientFromJson.getNom(), "Les noms doivent être identiques après conversion");
        assertEquals(client.getPrenom(), clientFromJson.getPrenom(), "Les prénoms doivent être identiques après conversion");
    }


}
