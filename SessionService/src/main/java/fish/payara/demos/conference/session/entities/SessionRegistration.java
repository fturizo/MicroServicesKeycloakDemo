package fish.payara.demos.conference.session.entities;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 * @author Fabio Turizo
 */
@Entity
@Table(name = "registrations")
@NamedQuery(name = "SessionRegistration.forUser", query = "select reg.session from SessionRegistration reg where reg.user = :user")
public class SessionRegistration implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @ManyToOne
    private Session session;
    
    @Column(name = "user_identity")
    private String user;

    public SessionRegistration(){        
    }
    
    public SessionRegistration(Session session, String user) {
        this.session = session;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public Session getSession() {
        return session;
    }

    public String getUser() {
        return user;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final SessionRegistration other = (SessionRegistration) obj;
        return Objects.equals(this.id, other.id);
    }
}
