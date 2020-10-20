package actions;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateTicketResponse {
    private String errorCode;
    private String errorDescription;
    private String ticketId;
}
