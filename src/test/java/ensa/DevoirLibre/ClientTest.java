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
        
        clientExistant = new Client(1, "John", "Doe", "123 St", "john@example.com", "123456789");
        clientInexistant = new Client(2, "Jane", "Smith", "456 St", "jane@example.com", "987654321");
        
        clients.add(clientExistant);
    }

    // TEST 1 : Ajout d'un client valide
    @Test
    public void testAjoutClientValide() {
        List<Client> clients = new ArrayList<>();
        Client client = new Client();

        client.AjoutClient(1, "John", "Doe", "123 Rue Exemple", "john.doe@example.com", "0123456789", clients);

        assertEquals(1, clients.size(), "La taille de la liste après ajout doit être 1");
        
        Client clientAjoute = clients.get(0);
        assertEquals(1, clientAjoute.getNumclient(), "Le numéro du client ajouté doit être 1");
        assertEquals("Doe", clientAjoute.getPrenom(), "Le prenom du client ajouté doit être John");
        assertEquals("John", clientAjoute.getNom(), "Le nom du client ajouté doit être Doe");
        assertEquals("123 Rue Exemple", clientAjoute.getAddresse(), "L'adresse du client ajouté doit être '123 Rue Exemple'");
        assertEquals("john.doe@example.com", clientAjoute.getEmail(), "L'email du client ajouté doit être 'john.doe@example.com'");
        assertEquals("0123456789", clientAjoute.getPhone(), "Le numéro de téléphone du client ajouté doit être '0123456789'");
        
        System.out.println("Client ajouté : " + clientAjoute.getPrenom() + " " + clientAjoute.getNom() + " à l'adresse " + clientAjoute.getAddresse());
        System.out.println("\n");

    }

    // TEST 2 : Numéro de client invalide
    @Test
    public void testAjoutClientNumeroClientInvalide() {
        List<Client> clients = new ArrayList<>();
        Client client = new Client();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            client.AjoutClient(0, "John", "Doe", "123 Rue Exemple", "john.doe@example.com", "0123456789", clients);
        });
        assertEquals("Le numéro de client doit être positif.", exception.getMessage(), "Le message d'exception pour numéro de client invalide doit être correct");
        System.out.println("\n");

    }

    // TEST 3 : Nom vide pour un client
    @Test
    public void testAjoutClientNomVide() {
        List<Client> clients = new ArrayList<>();
        Client client = new Client();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            client.AjoutClient(1, "", "Doe", "123 Rue Exemple", "john.doe@example.com", "0123456789", clients);
        });
        assertEquals("Le nom ne peut pas être vide.", exception.getMessage(), "Le message d'exception pour un nom vide doit être correct");
        System.out.println("\n");

    }

    // TEST 4 : Email invalide
    @Test
    public void testAjoutClientEmailInvalide() {
        List<Client> clients = new ArrayList<>();
        Client client = new Client();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            client.AjoutClient(1, "John", "Doe", "123 Rue Exemple", "john.doeexample.com", "0123456789", clients);
        });
        assertEquals("L'email ne peut pas être vide et doit contenir '@'.", exception.getMessage(), "Le message d'exception pour un email invalide doit être correct");
        System.out.println("\n");

    }
    
    // TEST 5 : Recherche d'un client existant
    @Test
    public void testRechercheClient_ClientExistant() {
        System.out.println("Test : Recherche d'un client existant");
        Client.RechercheClient(clients, clientExistant);  
    }

    // TEST 6 : Recherche d'un client inexistant
    @Test
    public void testRechercheClient_ClientInexistant() {
        System.out.println("Test : Recherche d'un client inexistant");
        Client.RechercheClient(clients, clientInexistant); 
    }

    // TEST 7 : Conversion d'un client en JSON
    @Test
    public void testClientToJson() {
        Client client = new Client(1, "John", "Doe", "123 St", "john@example.com", "123456789");
        String clientJson = client.toJson();
        System.out.println("Client en JSON : " + clientJson);
        assertNotNull(clientJson, "La conversion en JSON ne doit pas renvoyer null");
        System.out.println("\n");


        Client clientFromJson = Client.fromJson(clientJson);
        System.out.println("Client reconstruit depuis JSON : " + clientFromJson);
        assertEquals(client.getNumclient(), clientFromJson.getNumclient(), "Les numéros de client doivent être identiques après conversion");
        assertEquals(client.getNom(), clientFromJson.getNom(), "Les noms doivent être identiques après conversion");
        assertEquals(client.getPrenom(), clientFromJson.getPrenom(), "Les prénoms doivent être identiques après conversion");
        System.out.println("\n");

    }
    
    // TEST 8 : Ajout d'un client dans la base de données
    @Test
    public void testAjoutClient_BD() {
        String nom = "Durand";
        String prenom = "Pierre";
        String adresse = "1 Rue de Paris";
        String email = "pierre.durand@example.com";
        String phone = "0987654321";
        
        System.out.println("Test : Ajout d'un client dans la BD");


        Client client = new Client();
        client.AjoutClient_BD(nom, prenom, adresse, email, phone);
        System.out.println("\n");

    }

    // TEST 9 : Recherche d'un client par base de données
    @Test
    public void testRechercheClient_BD() {
        int numClientRecherche = 1;  
        System.out.println("Test : Recherche d'un client existant dans la BD");
        Client.RechercheClient_BD(numClientRecherche);
    }
}
