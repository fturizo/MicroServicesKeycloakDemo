package fish.payara.demos.conference.speaker.api;

import fish.payara.demos.conference.speaker.entitites.Speaker;
import fish.payara.demos.conference.speaker.services.SpeakerService;
import org.eclipse.microprofile.jwt.Claim;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HEAD;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;

/**
 *
 * @author Fabio Turizo
 */
@Path("/speaker")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("can-see-speakers")
public class SpeakerResource {    

    @Inject
    SpeakerService speakerService;

    @Context
    SecurityContext securityContext;

    @Inject
    @Claim("groups")
    Instance<Set<String>> currentGroups;

    @GET
    @Path("/{id}")
    public Response getSpeaker(@PathParam("id") String id) {
        return Optional.ofNullable(speakerService.get(id))
                .map(Response::ok)
                .orElse(Response.status(Status.NOT_FOUND))
                .build();
    }
    
    @GET
    @Path("/all")
    public List<Speaker> allSpeakers() {
        return speakerService.getAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("can-add-speakers")
    public Response addSpeaker(Speaker speaker, @Context UriInfo uriInfo) {
        if(currentGroups.get().contains("speaker")){
            speaker.setIdentity(securityContext.getUserPrincipal().getName());
        }
        var result = speakerService.save(speaker);
        return Response.created(UriBuilder.fromPath(uriInfo.getPath()).path("{id}").build(result.getId()))
                        .entity(speaker).build();
    }
    
    @POST
    @Path("/accept/{id}")
    @RolesAllowed("accept-speakers")
    public Response acceptSpeaker(@PathParam("id") String id){
        var speaker = speakerService.get(id).orElseThrow(() -> new NotFoundException("Speaker not found"));
        speakerService.save(speaker.accept());
        return Response.accepted().build();
    }
    
    @HEAD
    @PermitAll
    public Response checkSpeakers(@QueryParam("names") List<String> names){
        return (speakerService.allNamesExists(names) ? Response.ok() : Response.status(Status.NOT_FOUND)).build();
    }
}
