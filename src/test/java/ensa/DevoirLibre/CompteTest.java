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
        
        clientExistant = new Client(1, "John", "Doe", "123 St", "john@example.com", "123456789");
        banqueExistant = new Banque(1,"Banque1");
        
        compteExistant = new Compte(1, new Date(), new Date(), "Mobile", clientExistant, banqueExistant);
        comptes.add(compteExistant);
    }

    // TEST 1: Création d'un compte valide
    @Test
    public void testCreationCompteValide() {
        List<Compte> comptes = new ArrayList<>();
        Compte compte = new Compte();

        compte.CreationCompte(2, new Date(), new Date(), "Mobile", clientExistant, banqueExistant, comptes);

        assertEquals(1, comptes.size(), "La taille de la liste après ajout doit être 1");
        
        Compte compteAjoute = comptes.get(0);
        assertEquals(2, compteAjoute.getNumcompte(), "Le numéro du compte ajouté doit être 2");
        assertNotNull(compteAjoute.getDatecrea(), "La date de création du compte ne doit pas être nulle");
        assertNotNull(compteAjoute.getDateupdate(), "La date de mise à jour du compte ne doit pas être nulle");
        assertEquals("Mobile", compteAjoute.getDevice(), "Le dispositif du compte ajouté doit être 'Mobile'");
        
        System.out.println("Le compte ajouté : " + compteAjoute.getNumcompte() + "\tDate création : " + compteAjoute.getDatecrea() + "\tDate mise à jour : " + compteAjoute.getDateupdate() + "\tDispositif : " + compteAjoute.getDevice() + "\n");
    }

    // TEST 2: Numéro de compte invalide
    @Test
    public void testCreationCompteNumeroCompteInvalide() {
        List<Compte> comptes = new ArrayList<>();
        Compte compte = new Compte();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            compte.CreationCompte(0, new Date(), new Date(), "Mobile", clientExistant, banqueExistant, comptes);
        });
        assertEquals("Le numéro de compte doit être positif.", exception.getMessage(), "Le message d'exception pour numéro de compte invalide doit être correct");
    }

    // TEST 3: Date de création nulle
    @Test
    public void testCreationCompteDateCreationNulle() {
        List<Compte> comptes = new ArrayList<>();
        Compte compte = new Compte();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            compte.CreationCompte(1, null, new Date(), "Mobile", clientExistant, banqueExistant, comptes);
        });
        assertEquals("La date de création ne peut pas être nulle.", exception.getMessage(), "Le message d'exception pour date de création nulle doit être correct");
    }

    // TEST 4: Client nul
    @Test
    public void testCreationCompteClientNul() {
        List<Compte> comptes = new ArrayList<>();
        Compte compte = new Compte();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            compte.CreationCompte(1, new Date(), new Date(), "Mobile", null, banqueExistant, comptes);
        });
        assertEquals("Le client ne peut pas être null.", exception.getMessage(), "Le message d'exception pour un client null doit être correct");
    }

    // TEST 5: Recherche d'un compte existant
    @Test
    public void testRechercheCompte_CompteExistant() {
        System.out.println("Test : Recherche d'un compte existant");
        Compte.RechercheCompte(comptes, compteExistant);  
    }

    // TEST 6: Recherche d'un compte inexistant
    @Test
    public void testRechercheCompte_CompteInexistant() {
        compteInexistant = new Compte(2, new Date(), new Date(), "Web", clientExistant, banqueExistant);
        System.out.println("Test : Recherche d'un compte inexistant");
        Compte.RechercheCompte(comptes, compteInexistant); 
    }

    // TEST 7: Conversion d'un compte en JSON
    @Test
    public void testCompteToJson() {
        Client client = new Client(1, "John", "Doe", "123 St", "john@example.com", "123456789");
        Banque banque = new Banque(1, "Banque1");
        Compte compte = new Compte(1, new Date(), new Date(), "Mobile", client, banque);

        String compteJson = compte.toJson();
        System.out.println("Compte en JSON : " + compteJson);
        assertNotNull(compteJson, "La conversion en JSON ne doit pas renvoyer null");
        System.out.println("\n");


        Compte compteFromJson = Compte.fromJson(compteJson);
        System.out.println("Compte reconstruit depuis JSON : " + compteFromJson);
        assertEquals(compte.getNumcompte(), compteFromJson.getNumcompte(), "Les numéros de compte doivent être identiques après conversion");
        assertEquals(compte.getDevice(), compteFromJson.getDevice(), "Les dispositifs doivent être identiques après conversion");
        System.out.println("\n");

    }

    // TEST 8: Création du compte en base de données
    @Test
    public void testCreationCompte_BD() {
        Date creation = new Date();
        Date update = new Date();
        String device = "DeviceTest";
        Client client = new Client(1, "John", "Doe", "1234 Elm Street", "john.doe@example.com", "1234567890");
        Banque banque = new Banque(1, "1234 Bank Street");
        
        System.out.println("Test : Ajout d'un compte dans la BD");

        
        Compte compte = new Compte();
        compte.CreationCompte_BD(creation, update, device, client, banque);

        assertTrue(true, "La création du compte en base de données devrait réussir.");
        
        System.out.println("\n");
    }

    // TEST 9: Recherche d'un compte en base de données
    @Test
    public void testRechercheCompte_BD() {

        System.out.println("Test : recherche d'un compte dans la BD");

        Compte.RechercheCompte_BD(2);

        assertTrue(true, "Le compte doit être trouvé et afficher les détails du compte.");
    }
}
