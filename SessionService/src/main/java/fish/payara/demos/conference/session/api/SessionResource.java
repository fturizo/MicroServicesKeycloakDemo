package fish.payara.demos.conference.session.api;

import fish.payara.demos.conference.session.entities.Session;
import fish.payara.demos.conference.session.services.SessionService;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

/**
 *
 * @author Fabio Turizo
 */
@Path("/session")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("can-see-sessions")
public class SessionResource {

    @Inject
    SessionService sessionService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("can-create-sessions")
    public Response create(Session session) {
        session = sessionService.addNewSession(session);
        return Response.created(URI.create("/" + session.getId()))
                .entity(session).build();
    }
    
    @GET
    @Path("/all")
    public List<Session> getAll() {
        return sessionService.retrieveSessions();
    }

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") String id) {
        return sessionService.retrieve(id).map(Response::ok)
                .orElse(Response.status(Status.NOT_FOUND)).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("can-delete-sessions")
    public Response delete(@PathParam("id") String id) {
        var session = sessionService.retrieve(id);
        if (session.isPresent()) {
            sessionService.delete(session.get());
            return Response.accepted().build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/date/{date}")
    public List<Session> forDate(@PathParam("date") String date) {
        return sessionService.retrieve(LocalDate.parse(date));
    }
}
