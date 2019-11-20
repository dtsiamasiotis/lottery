package events;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ChargeEvent implements Serializable {
    private String msisdn;
    private String amount;
    private long ticketId;
}
