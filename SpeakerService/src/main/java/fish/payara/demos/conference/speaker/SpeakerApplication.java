package fish.payara.demos.conference.speaker;

import jakarta.annotation.security.DeclareRoles;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.auth.LoginConfig;

/**
 *
 * @author Fabio Turizo
 */
@ApplicationPath("/")
@ApplicationScoped
@LoginConfig(authMethod = "MP-JWT")
@DeclareRoles({"Admin", "Speaker"})
public class SpeakerApplication extends Application{
}
