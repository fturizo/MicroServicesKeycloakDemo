package fish.payara.demos.conference.speaker.api;

import java.io.IOException;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

/**
 *
 * @author Fabio Turizo
 */
@Provider
public class CorsProcessingFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        if(responseContext.getHeaderString("Access-Control-Allow-Origin") == null){
            responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
            responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
            responseContext.getHeaders().add("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, Authorization");
            responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, OPTIONS, DELETE, HEAD");
        }
    }
}
