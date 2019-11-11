import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "restRequestsLog")
@Getter
@Setter
public class RestRequestsLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String incomingRequest;
    private String response;
    private Date incomingRequestTstamp;
    private Date responseTstamp;
}