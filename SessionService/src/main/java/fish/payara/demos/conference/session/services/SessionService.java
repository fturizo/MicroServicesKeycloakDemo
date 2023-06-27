package fish.payara.demos.conference.session.services;

import fish.payara.demos.conference.session.entities.Session;
import fish.payara.demos.conference.session.entities.SessionRegistration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

/**
 *
 * @author Fabio Turizo
 */
@ApplicationScoped
public class SessionService {
    
    @PersistenceContext(unitName = "Session")
    EntityManager em;
    
    @Transactional
    public Session addNewSession(Session session){        
        em.persist(session);
        em.flush();
        return session;
    }
    
    @Transactional
    public void delete(Session session){
        em.remove(em.find(Session.class, session.getId()));
        em.flush();
    }
    
    public Optional<Session> retrieve(String id){
        return Optional.ofNullable(em.find(Session.class, id));
    }
    
    @Transactional
    public SessionRegistration register(Session session, String user){
        var registration = new SessionRegistration(session, user);
        em.persist(registration);
        em.flush();
        return registration;
    }
    
    public List<Session> retrieve(LocalDate date){
        return em.createNamedQuery("Session.getForDay", Session.class).setParameter("date", date).getResultList();
    }
    
    public List<Session> retrieveSessions(){
        return em.createNamedQuery("Session.all", Session.class).getResultList();
    }
    
    public List<Session> retrieveRegisteredSessions(String user){
        return em.createNamedQuery("SessionRegistration.forUser", Session.class)
                 .setParameter("user", user)
                 .getResultList();
    }
}
