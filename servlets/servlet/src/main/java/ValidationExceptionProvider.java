import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ValidationExceptionProvider implements ExceptionMapper<ValidationException> {

    public Response toResponse(ValidationException e) {
        return Response.status(Response.Status.BAD_REQUEST).entity("missing parameter").build();
    }
}
