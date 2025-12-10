package agenda;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Termination {
    //attributs
    private final LocalDate myStart;
    private final ChronoUnit myFrequency;
    private final LocalDate myTerminationDate;
    private final long myNumberOfOccurrences;

    //Constructeur
    public Termination(LocalDate start, ChronoUnit frequency, LocalDate terminationInclusive) {
        this.myStart = start;
        this.myFrequency = frequency;
        this.myTerminationDate = terminationInclusive;
        long diff = frequency.between(start, terminationInclusive);
        this.myNumberOfOccurrences = diff + 1;
    }

    public Termination(LocalDate start, ChronoUnit frequency, long numberOfOccurrences) {
        this.myStart = start;
        this.myFrequency = frequency;
        this.myNumberOfOccurrences = numberOfOccurrences;
        this.myTerminationDate = start.plus(numberOfOccurrences - 1, frequency);
    }

    public LocalDate terminationDateInclusive() {
        return myTerminationDate;
    }

    public long numberOfOccurrences() {
        return myNumberOfOccurrences;
    }
}