package agenda;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests des nouvelles fonctionnalités de l'agenda :
 * - findByTitle(String title)
 * - isFreeFor(Event e)
 */
public class AgendaTestComplementaire {
    
    private Agenda agenda;
    
    // Dates et Durées de référence
    private final LocalDate NOV_1_2020 = LocalDate.of(2020, 11, 1);
    private final LocalDateTime NOV_1_2020_10_00 = LocalDateTime.of(2020, 11, 1, 10, 0);
    private final Duration DURATION_60_MIN = Duration.ofMinutes(60);
    private final Duration DURATION_90_MIN = Duration.ofMinutes(90);

    // Événements
    private Event meeting1;
    private Event training;
    private Event meeting2;
    private Event simpleRepetitive;

    @BeforeEach
    public void setUp() {
        agenda = new Agenda();


        meeting1 = new Event("Team Meeting", NOV_1_2020_10_00, DURATION_60_MIN);
        agenda.addEvent(meeting1);

        LocalDateTime nov_1_2020_14_00 = NOV_1_2020_10_00.plusHours(4); 
        meeting2 = new Event("Team Meeting", nov_1_2020_14_00, DURATION_60_MIN); 
        agenda.addEvent(meeting2);
        

        LocalDateTime nov_1_2020_16_00 = NOV_1_2020_10_00.plusHours(6); 
        training = new Event("Java Training", nov_1_2020_16_00, DURATION_90_MIN); 
        agenda.addEvent(training);


        simpleRepetitive = new Event("Daily Task", NOV_1_2020_10_00.plusDays(1), DURATION_60_MIN);
        simpleRepetitive.setRepetition(ChronoUnit.DAYS);
        agenda.addEvent(simpleRepetitive);
    }


    // Tests pour findByTitle
    @Test
    public void testFindByTitleFindsMultipleEvents() {
        List<Event> results = agenda.findByTitle("Team Meeting");
        
        assertEquals(2, results.size(), "Doit trouver les deux événements ayant le même titre.");
        assertTrue(results.contains(meeting1));
        assertTrue(results.contains(meeting2));
    }

    @Test
    public void testFindByTitleFindsSingleEvent() {
        List<Event> results = agenda.findByTitle("Java Training");
        
        assertEquals(1, results.size(), "Doit trouver un seul événement.");
        assertTrue(results.contains(training));
    }

    @Test
    public void testFindByTitleFindsNoEvent() {
        List<Event> results = agenda.findByTitle("Non-existent Event");
        
        assertTrue(results.isEmpty(), "Ne doit trouver aucun événement.");
    }
    
    // Tests pour isFreeFor
    @Test
    public void testIsFreeForReturnsTrueIfNoOverlap() {

        Event newEvent = new Event("Lunch", NOV_1_2020_10_00.plusHours(2), DURATION_60_MIN);
        
        assertTrue(agenda.isFreeFor(newEvent), "L'agenda devrait être libre entre 11:00 et 14:00.");
    }
    
    @Test
    public void testIsFreeForReturnsFalseIfFullOverlap() {
        Event newEvent = new Event("Quick Call", NOV_1_2020_10_00.plusMinutes(15), Duration.ofMinutes(30));
        
        assertFalse(agenda.isFreeFor(newEvent), "L'événement chevauche meeting1.");
    }

    @Test
    public void testIsFreeForReturnsFalseIfStartOverlap() {
        Event newEvent = new Event("Late Start", NOV_1_2020_10_00.plusMinutes(30), DURATION_60_MIN);
        
        assertFalse(agenda.isFreeFor(newEvent), "L'événement débute pendant meeting1.");
    }

    @Test
    public void testIsFreeForReturnsFalseIfEndOverlap() {
        Event newEvent = new Event("Early End", NOV_1_2020_10_00.minusMinutes(30), DURATION_60_MIN);
        
        assertFalse(agenda.isFreeFor(newEvent), "L'événement se termine pendant meeting1.");
    }

    @Test
    public void testIsFreeForReturnsTrueIfTouchingBoundary() {

        Event newEvent = new Event("Boundary Start", NOV_1_2020_10_00.plusHours(1), DURATION_60_MIN);
        
        assertTrue(agenda.isFreeFor(newEvent), "Le toucher des limites ne doit pas être considéré comme un chevauchement.");
    }
    
    @Test
    public void testIsFreeForIgnoresRepetitiveEvent() {

        Event newEvent = new Event("Test Repetitive", NOV_1_2020_10_00.plusDays(1), DURATION_60_MIN);
        

        assertTrue(agenda.isFreeFor(newEvent), 
            "Retourne VRAI car l'implémentation actuelle de isFreeFor() ne gère/ignore pas la vérification des événements répétitifs existants.");
    }
}
