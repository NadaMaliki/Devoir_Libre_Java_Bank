package ensa.DevoirLibre;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TransactionTest {

    private List<Transaction> transactions;
    private Compte compteSource;
    private Compte compteDestination;
    private Transaction transactionExistante;
    private Transaction transactionInexistante;
    
    @Test
    public void testAffectationAutomatiqueDuType() {
        // Création de comptes pour les tests
        Compte compteSource = new Compte();
        compteSource.setBanque(new Banque(1, "France"));
        
        Compte compteDestination1 = new Compte();
        compteDestination1.setBanque(new Banque(1, "France")); // Même banque et même pays
        
        Compte compteDestination2 = new Compte();
        compteDestination2.setBanque(new Banque(2, "France")); // Même pays mais banques différentes
        
        Compte compteDestination3 = new Compte();
        compteDestination3.setBanque(new Banque(3, "Espagne")); // Pays différents
        
        List<Compte> comptesDst = new ArrayList<>();
        
        // Test VIRINT (Transaction interne entre deux comptes de même banque et même pays)
        comptesDst.clear();
        comptesDst.add(compteDestination1);
        Transaction transaction1 = new Transaction(Transtype.VIRINT, LocalDateTime.now(), "REF001", compteSource, comptesDst);
        assertEquals(Transtype.VIRINT, transaction1.getType(), "Le type de transaction devrait être VIRINT.");
        
        // Test VIREST (Transaction entre deux banques différentes mais dans le même pays)
        comptesDst.clear();
        comptesDst.add(compteDestination2);
        Transaction transaction2 = new Transaction(Transtype.VIREST, LocalDateTime.now(), "REF002", compteSource, comptesDst);
        assertEquals(Transtype.VIREST, transaction2.getType(), "Le type de transaction devrait être VIREST.");
        
        // Test VIRCHAC (Transaction entre deux banques et deux pays différents)
        comptesDst.clear();
        comptesDst.add(compteDestination3);
        Transaction transaction3 = new Transaction(Transtype.VIRCHAC, LocalDateTime.now(), "REF003", compteSource, comptesDst);
        assertEquals(Transtype.VIRCHAC, transaction3.getType(), "Le type de transaction devrait être VIRCHAC.");
        
        // Test VIRTMULTA (Transaction impliquant plusieurs comptes de destination)
        comptesDst.clear();
        comptesDst.add(compteDestination1);
        comptesDst.add(compteDestination2);
        Transaction transaction4 = new Transaction(Transtype.VIRTMULTA, LocalDateTime.now(), "REF004", compteSource, comptesDst);
        assertEquals(Transtype.VIRTMULTA, transaction4.getType(), "Le type de transaction devrait être VIRTMULTA.");
        
        // Affichage des transactions pour vérification
        System.out.println("Transaction 1: " + transaction1);
        System.out.println("Transaction 2: " + transaction2);
        System.out.println("Transaction 3: " + transaction3);
        System.out.println("Transaction 4: " + transaction4);
    }

    @BeforeEach
    public void setUp() {
        transactions = new ArrayList<>();
        
        // Initialize accounts for testing
        Client client = new Client(1, "John", "Doe", "123 St", "john@example.com", "123456789");
        Banque banque = new Banque(1, "Banque1");
        
        compteSource = new Compte(1, new Date(), new Date(), "Mobile", client, banque);
        compteDestination = new Compte(2, new Date(), new Date(), "Web", client, banque);
        
        // Create an existing transaction
        List<Compte> comptesDst = new ArrayList<>();
        comptesDst.add(compteDestination);
        transactionExistante = new Transaction(Transtype.VIRINT, LocalDateTime.now(), "REF123", compteSource, comptesDst);
        transactions.add(transactionExistante);
    }

    @Test
    public void testCreationTransactionValide() {
        List<Compte> comptesDst = new ArrayList<>();
        comptesDst.add(compteDestination);
        
        Transaction newTransaction = new Transaction(Transtype.VIRINT, LocalDateTime.now(), "REF456", compteSource, comptesDst);
        transactions.add(newTransaction);

        assertEquals(2, transactions.size(), "The list size should be 2 after adding a new transaction");

        // Verify the details of the added transaction
        Transaction transactionAjoutee = transactions.get(1);
        assertEquals("REF456", transactionAjoutee.getReference(), "The reference of the added transaction should be 'REF456'");
        assertNotNull(transactionAjoutee.getTimestemp(), "The transaction date should not be null");
        
        System.out.println("Transaction added:   Reference: " + transactions.get(0).getReference() + "  type: " + transactions.get(0).getType() + "  compte source: " + transactions.get(0).getCompte_src());
    }


    @Test
    public void testRechercheTransaction_TransactionExistante() {
        // Search for an existing transaction
        assertTrue(transactions.contains(transactionExistante), "The existing transaction should be found");
    }

    @Test
    public void testRechercheTransaction_TransactionInexistante() {
        // Create a non-existent transaction
        transactionInexistante = new Transaction(Transtype.VIRINT, LocalDateTime.now(), "REF999", compteSource, Arrays.asList(compteDestination));
        
        // Search for a non-existent transaction
        assertFalse(transactions.contains(transactionInexistante), "The non-existent transaction should not be found");
    }
    
    @Test
    public void testTransactionToJson() {
        // Créez un compte source et des comptes de destination
        Client client = new Client(1, "John", "Doe", "123 St", "john@example.com", "123456789");
        Banque banque = new Banque(1, "Banque1");
        Compte compteSource = new Compte(1, new Date(), new Date(), "Mobile", client, banque);
        Compte compteDest = new Compte(2, new Date(), new Date(), "Web", client, banque);

        List<Compte> comptesDest = new ArrayList<>();
        comptesDest.add(compteDest);

        // Créez une transaction
        Transaction transaction = new Transaction(Transtype.VIRINT, LocalDateTime.now(), "REF123", compteSource, comptesDest);

        // Convertissez la transaction en JSON
        String transactionJson = transaction.toJson();
        System.out.println("Transaction en JSON : " + transactionJson);
        assertNotNull(transactionJson, "La conversion en JSON ne doit pas renvoyer null");

        // Conversion inverse pour vérifier la cohérence
        Transaction transactionFromJson = Transaction.fromJson(transactionJson);        
        System.out.println("Transaction reconstruite depuis JSON : " + transactionFromJson.toString());
        assertEquals(transaction.getReference(), transactionFromJson.getReference(), "Les références doivent être identiques après conversion");
        assertEquals(transaction.getType(), transactionFromJson.getType(), "Les types de transaction doivent être identiques après conversion");
    }

    
    
    

}
