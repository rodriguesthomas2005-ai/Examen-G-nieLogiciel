package agenda;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime; 
import java.time.temporal.ChronoUnit;
import java.time.temporal.UnsupportedTemporalTypeException;

public class Event {
    //attributs
    private String myTitle;
    private LocalDateTime myStart;
    private Duration myDuration;
    private Repetition rep = null;

    /**
     * Constructeur
     *
     * @param title the title of this event
     * @param start the start time of this event
     * @param duration the duration of this event
     */
    public Event(String title, LocalDateTime start, Duration duration) {
        this.myTitle = title;
        this.myStart = start;
        this.myDuration = duration;
    }

    //getter et setter
    /**
     * @return the myTitle
     */
    public String getTitle() {
        return myTitle;
    }
    /**
     * @return the myStart
     */
    public LocalDateTime getStart() {
        return myStart;
    }
    /**
     * @return the myDuration
     */
    public LocalDateTime getEnd() {
        return myStart.plus(myDuration);
    }

    public Repetition getRepetition() {
        return rep;
    }

    public void setRepetition(ChronoUnit frequency) {
        this.rep = new Repetition(frequency);
    }
    

    //methodes
    public void addException(LocalDate date) {
        if (rep != null) {
            rep.addException(date);
        }
    }
    
    public void setTermination(LocalDate terminationInclusive) {
        if (rep != null) {
            Termination termination = new Termination(myStart.toLocalDate(), rep.getFrequency(), terminationInclusive);
            rep.setTermination(termination);
        }
    }
    
    public void setTermination(long numberOfOccurrences) {
        if (rep != null) {
            Termination termination = new Termination(myStart.toLocalDate(), rep.getFrequency(), numberOfOccurrences);
            rep.setTermination(termination);
        }
    }
    
    public long getNumberOfOccurrences() {
        if (rep != null && rep.getTermination() != null) {
            return rep.getTermination().numberOfOccurrences();
        }
        return 1;
    }
    
    public LocalDate getTerminationDate() {
        if (rep != null && rep.getTermination() != null) {
            return rep.getTermination().terminationDateInclusive();
        }
        return null;
    }
    
    /**
     * Tests if an event occurs on a given day
     *
     * @param aDay the day to test
     * @return true if the event occurs on that day, false otherwise
     */
    public boolean isInDay(LocalDate aDay) {
        LocalDate startDate = myStart.toLocalDate();
        
        //simple rep
        if (rep == null) {

            LocalDateTime endDateTime = myStart.plus(myDuration);
            LocalDate endDate = endDateTime.toLocalDate(); 

            return !aDay.isBefore(startDate) && !aDay.isAfter(endDate);
        }

        //multi rep
        if (aDay.isBefore(startDate)) {
            return false;
        }
        

        if (rep.getTermination() != null) {
            LocalDate terminationDate = rep.getTermination().terminationDateInclusive();
            

            if (aDay.isAfter(terminationDate.plusDays(1))) {
                return false;
            }
        }
        

        ChronoUnit frequency = rep.getFrequency();
        long distance = 0;
        
        try {

            distance = startDate.until(aDay, frequency);
        } catch (UnsupportedTemporalTypeException e) {
            distance = frequency.between(startDate, aDay);
        }
        

        boolean isOccurrenceDay = (distance >= 0 && distance % 1 == 0 && startDate.plus(distance, frequency).equals(aDay));
        

        if (isOccurrenceDay) {
            return !rep.isException(aDay);
        }

        LocalDate previousDay = aDay.minusDays(1);
        
        long distancePrevious = 0;
        try {
            distancePrevious = startDate.until(previousDay, frequency);
        } catch (UnsupportedTemporalTypeException e) {
            distancePrevious = frequency.between(startDate, previousDay);
        }

        boolean isPreviousDayOccurrence = (distancePrevious >= 0 && distancePrevious % 1 == 0 && startDate.plus(distancePrevious, frequency).equals(previousDay));
        
        if (isPreviousDayOccurrence) {

            LocalDateTime occurrenceStart = myStart.plus(distancePrevious, frequency);
            LocalDateTime occurrenceEnd = occurrenceStart.plus(myDuration);
            

            if (occurrenceEnd.toLocalDate().equals(aDay)) {
                
                if (!rep.isException(previousDay)) {
                    
                    
                    if (rep.getTermination() != null) {
                        if (previousDay.isAfter(rep.getTermination().terminationDateInclusive())) {
                           return false;
                        }
                    }
                    return true;

                }
            }
        }
        
        return false;
    }
    
    
    //toString
    @Override
    public String toString() {
        return "Event [myTitle=" + myTitle + ", myStart=" + myStart + "]";
    }
}