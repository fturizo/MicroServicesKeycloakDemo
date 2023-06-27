package fish.payara.demos.conference.speaker.services;

import fish.payara.demos.conference.speaker.entitites.Speaker;
import java.util.logging.Logger;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

/**
 * @author Fabio Turizo
 */
@ApplicationScoped
public class SpeakerInitializer {

    private static final Logger LOG = Logger.getLogger(SpeakerInitializer.class.getName());
    
    @Inject
    SpeakerService speakerService;
    
    public void initialize(@Observes @Initialized(ApplicationScoped.class) Object event){
        LOG.info("Initializing speakers");
        if(speakerService.getAll().isEmpty()){
            speakerService.save(new Speaker("John Doe", "Payara Services Ltd.").accept());
            speakerService.save(new Speaker("Adam Fullbright", "Payara Tech").accept());
        }
    }
}
