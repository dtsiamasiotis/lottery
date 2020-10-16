package events;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class PromoEvent implements Serializable {
    private String msisdn;
    private String message;
}
