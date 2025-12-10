package agenda;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;

public class Repetition {
    
    // attribut
    private Termination term = null;
    private final ChronoUnit Freq;
    private final Set<LocalDate> excepts = new HashSet<>();

    //methodes
    public Repetition(ChronoUnit myFrequency) {
        this.Freq = myFrequency;
    }

    public ChronoUnit getFrequency() {
        return Freq;
    }
    
    public Termination getTermination() {
        return term;
    }

    /**
     * Les exceptions à la répétition
     * @param date un date à laquelle l'événement ne doit pas se répéter
     */
    public void addException(LocalDate date) {
        this.excepts.add(date);
    }


    /**
     * La terminaison d'une répétition (optionnelle)
     * @param termination la terminaison de la répétition
     */
    public void setTermination(Termination termination) {
        this.term = termination;
    }

    public boolean isException(LocalDate date) {
        return excepts.contains(date);
    }
}