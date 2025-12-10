package agenda;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Description : An agenda that stores events
 */
public class Agenda {
    //attributs
    private final List<Event> evenement = new ArrayList<>();

    /**
     * Adds an event to this agenda
     *
     * @param e the event to add
     */
    public void addEvent(Event e) {
        this.evenement.add(e);

    }

    /**
     * Computes the events that occur on a given day
     *
     * @param day the day toi test
     * @return a list of events that occur on that day
     */
    public List<Event> eventsInDay(LocalDate day) {
        List<Event> eventsToday = new ArrayList<>();

        for (Event event : evenement) {

            if (event.isInDay(day)) {
                eventsToday.add(event);
            }
        }

        return eventsToday;
    }
    
    /**
     * Trouver les événements de l'agenda en fonction de leur titre
     * @param title le titre à rechercher
     * @return les événements qui ont le même titre
     */
    public List<Event> findByTitle(String title) {
        List<Event> matchingEvents = new ArrayList<>();
        
        for (Event event : evenement) {
            if (event.getTitle().equals(title)) {
                matchingEvents.add(event);

            }
        }

        return matchingEvents;
    }
    
    /**
     * Déterminer s’il y a de la place dans l'agenda pour un événement (aucun autre événement au même moment)
     * @param e L'événement à tester (on se limitera aux événements sans répétition)
     * @return vrai s’il y a de la place dans l'agenda pour cet événement
     */
    public boolean isFreeFor(Event e) {
        LocalDateTime newStart = e.getStart();
        LocalDateTime newEnd = e.getEnd();
        
        for (Event existingEvent : evenement) {
            if (existingEvent.getRepetition() == null) {
                
                LocalDateTime existingStart = existingEvent.getStart();
                LocalDateTime existingEnd = existingEvent.getEnd();
                
                boolean overlaps = newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart);

                if (overlaps) {
                    return false; // conflit trouvé si le nouvel evenement commence avant que l'evenement existant ne se termine et que le nouvel evenement se termine après que l'evenement existant ait commencee.
                }
            } 
        }

        return true;
    }
}