package actions;

import javax.validation.ValidationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import actions.ErrorResponse;
import org.jboss.resteasy.api.validation.ResteasyConstraintViolation;
import org.jboss.resteasy.api.validation.ResteasyViolationException;

import java.util.List;

@Provider
public class ValidationExceptionProvider implements ExceptionMapper<ValidationException> {

    public Response toResponse(ValidationException e) {
        if (e instanceof ResteasyViolationException) {
            ResteasyViolationException resteasyViolationException = ResteasyViolationException.class.cast(e);
            Exception ex = resteasyViolationException.getException();
            List<List<ResteasyConstraintViolation>> violationLists = ((ResteasyViolationException) e).getViolationLists();
            String message = buildMessage(violationLists);
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setErrorDescription(message);
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).type(MediaType.APPLICATION_JSON_TYPE).build();
        }
        else
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
    }

    private String buildMessage(List<List<ResteasyConstraintViolation>> violationLists) {
        StringBuilder errorMessage = new StringBuilder();
        for(List<ResteasyConstraintViolation> l:violationLists)
        {
            for (ResteasyConstraintViolation v:l)
            {
                errorMessage.append(v.getMessage()).append(".");
            }
        }

        return errorMessage.toString();
    }
}
