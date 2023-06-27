package fish.payara.demos.conference.session.entities;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Objects;
import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 *
 * @author Fabio Turizo
 */
@Entity
@NamedQuery(name = "Session.all",
            query = "select s from Session s order by s.sessionDate")
@NamedQuery(name = "Session.getForDay", 
            query = "select s from Session s where s.sessionDate = :date order by s.title")
@Table(name = "sessions")
public class Session implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    private String title;
    private String venue;
    private LocalDate sessionDate;
    private Duration duration;

    public Session() {
    }

    @JsonbCreator
    public Session(@JsonbProperty("title") String title,
                   @JsonbProperty("venue") String venue,
                   @JsonbProperty("date") LocalDate date,
                   @JsonbProperty("duration") Duration duration) {
        this.title = title;
        this.sessionDate = date;
        this.venue = venue;
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getVenue() {
        return venue;
    }

    @JsonbProperty("date")
    public LocalDate getSessionDate() {
        return sessionDate;
    }

    public Duration getDuration() {
        return duration;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Session other = (Session) obj;
        return Objects.equals(this.id, other.id);
    }
}
