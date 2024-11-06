package ensa.DevoirLibre;

import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class CompteTest {

    private List<Compte> comptes;
    private Client clientExistant;
    private Banque banqueExistant;
    private Compte compteExistant;
    private Compte compteInexistant;

    @BeforeEach
    public void setUp() {
        comptes = new ArrayList<>();
        
        // Initialisation des clients et banques pour les tests
        clientExistant = new Client(1, "John", "Doe", "123 St", "john@example.com", "123456789");
        banqueExistant = new Banque(1,"Banque1");
        
        // Création d'un compte existant
        compteExistant = new Compte(1, new Date(), new Date(), "Mobile", clientExistant, banqueExistant);
        comptes.add(compteExistant);
    }

    @Test
    public void testCreationCompteValide() {
        List<Compte> comptes = new ArrayList<>();
        Compte compte = new Compte();

        // Création d'un compte valide
        compte.CreationCompte(2, new Date(), new Date(), "Mobile", clientExistant, banqueExistant, comptes);

        // Vérification que la taille de la liste a bien augmenté de 1
        assertEquals(1, comptes.size(), "La taille de la liste après ajout doit être 1");

        // Vérification des informations du compte ajouté
        Compte compteAjoute = comptes.get(0);
        assertEquals(2, compteAjoute.getNumcompte(), "Le numéro du compte ajouté doit être 2");
        assertNotNull(compteAjoute.getDatecrea(), "La date de création du compte ne doit pas être nulle");
        assertNotNull(compteAjoute.getDateupdate(), "La date de mise à jour du compte ne doit pas être nulle");
        assertEquals("Mobile", compteAjoute.getDevice(), "Le dispositif du compte ajouté doit être 'Mobile'");
        
        System.out.println("Le compte: " + comptes.get(0).getNumcompte() + " \t date creation" + comptes.get(0).getDatecrea()+ "\t date update: " + comptes.get(0).getDateupdate() + "\t Device: " + comptes.get(0).getDevice() +  "\t est bien ajouté."  );

    }

    @Test
    public void testCreationCompteNumeroCompteInvalide() {
        List<Compte> comptes = new ArrayList<>();
        Compte compte = new Compte();
        System.out.println("test");
        // Test avec un numéro de compte invalide (<= 0)
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            compte.CreationCompte(0, new Date(), new Date(), "Mobile", clientExistant, banqueExistant, comptes);
        });
        assertEquals("Le numéro de compte doit être positif.", exception.getMessage(), "Le message d'exception pour numéro de compte invalide doit être correct");
    }

    @Test
    public void testCreationCompteDateCreationNulle() {
        List<Compte> comptes = new ArrayList<>();
        Compte compte = new Compte();

        // Test avec une date de création nulle
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            compte.CreationCompte(1, null, new Date(), "Mobile", clientExistant, banqueExistant, comptes);
        });
        assertEquals("La date de création ne peut pas être nulle.", exception.getMessage(), "Le message d'exception pour date de création nulle doit être correct");
    }

    @Test
    public void testCreationCompteClientNul() {
        List<Compte> comptes = new ArrayList<>();
        Compte compte = new Compte();

        // Test avec un client nul
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            compte.CreationCompte(1, new Date(), new Date(), "Mobile", null, banqueExistant, comptes);
        });
        assertEquals("Le client ne peut pas être null.", exception.getMessage(), "Le message d'exception pour un client null doit être correct");
    }

    @Test
    public void testRechercheCompte_CompteExistant() {
        // Recherche d'un compte existant 
        Compte.RechercheCompte(comptes, compteExistant);  
    }

    @Test
    public void testRechercheCompte_CompteInexistant() {
        // Création d'un compte inexistant
        compteInexistant = new Compte(2, new Date(), new Date(), "Web", clientExistant, banqueExistant);
        
        // Recherche d'un compte inexistant
        Compte.RechercheCompte(comptes, compteInexistant); 
    }
    
    @Test
    public void testCompteToJson() {
        // Initialisez un client et une banque pour le compte
        Client client = new Client(1, "John", "Doe", "123 St", "john@example.com", "123456789");
        Banque banque = new Banque(1, "Banque1");

        // Créez un compte avec des détails de base
        Compte compte = new Compte(1, new Date(), new Date(), "Mobile", client, banque);

        // Convertissez le compte en JSON
        String compteJson = compte.toJson();
        System.out.println("Compte en JSON : " + compteJson);
        assertNotNull(compteJson, "La conversion en JSON ne doit pas renvoyer null");

        // Conversion inverse pour vérifier la cohérence
        Compte compteFromJson = Compte.fromJson(compteJson);      
        System.out.println("Compte reconstruite depuis JSON : " + compteJson.toString());
        assertEquals(compte.getNumcompte(), compteFromJson.getNumcompte(), "Les numéros de compte doivent être identiques après conversion");
        assertEquals(compte.getDevice(), compteFromJson.getDevice(), "Les dispositifs doivent être identiques après conversion");
    }

}
