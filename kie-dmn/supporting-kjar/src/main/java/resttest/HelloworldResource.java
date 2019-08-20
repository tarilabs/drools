package resttest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.kie.dmn.api.core.DMNResult;
import test.TheDMNContext;
import test.TheDMNResult;

@Path("/helloworld")
public class HelloworldResource {

    @POST()
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public @ApiResponse(content = @Content(schema = @Schema(implementation = TheDMNResult.class))) DMNResult dummy(TheDMNContext payload) {
        return null;
    }

}
